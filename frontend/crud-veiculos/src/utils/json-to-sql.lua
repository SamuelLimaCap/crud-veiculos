#!/usr/bin/env lua
--[[
  Convert JSON file with nested car data to SQL INSERT statements
  Usage: lua json-to-sql.lua input.json [output.sql]
]]

local json = require("cjson")

-- Check command line arguments
if #arg < 1 then
    print("Usage: lua json-to-sql.lua <input.json> [output.sql]")
    os.exit(1)
end

local jsonFile = arg[1]
local outputFile = arg[2] or "output.sql"

-- Read JSON file
local file = io.open(jsonFile, "r")
if not file then
    print("Error: File '" .. jsonFile .. "' not found")
    os.exit(1)
end

local jsonContent = file:read("*a")
file:close()

-- Parse JSON
local ok, data = pcall(function() return json.decode(jsonContent) end)
if not ok then
    print("Error: Invalid JSON file")
    os.exit(1)
end

if not data or type(data) ~= "table" or #data == 0 then
    print("Error: JSON must contain data")
    os.exit(1)
end

-- Escape SQL values
local function escapeSqlValue(val)
    if val == nil or val == json.null then
        return "NULL"
    elseif type(val) == "boolean" then
        return val and "1" or "0"
    elseif type(val) == "number" then
        return tostring(val)
    else
        local str = tostring(val)
        str = str:gsub("'", "''")
        return "'" .. str .. "'"
    end
end

-- Escape column names (PostgreSQL uses double quotes)
local function escapeColumnName(name)
    return '"' .. name .. '"'
end

-- Generate SQL statements
local sqlStatements = {}

-- Insert brands
for _, brand in ipairs(data) do
    if type(brand) == "table" and brand.marca then
        table.insert(sqlStatements, 
            "INSERT INTO marcas (marca) VALUES (" .. escapeSqlValue(brand.marca) .. ");")
        
        -- Insert models for this brand
        if brand.modelo and type(brand.modelo) == "table" then
            for _, model in ipairs(brand.modelo) do
                if type(model) == "table" then
                    local columns = {"marca", "label", "\"value\""}
                    local values = {
                        escapeSqlValue(brand.marca),
                        escapeSqlValue(model.Label),
                        escapeSqlValue(model.Value)
                    }
                    
                    -- Debug: ensure marca is properly escaped as string
                    -- print("marca: " .. values[1])
                    
                    local stmt = "INSERT INTO modelos (" .. table.concat(columns, ", ") .. 
                                 ") VALUES (" .. table.concat(values, ", ") .. ");"
                    table.insert(sqlStatements, stmt)
                end
            end
        end
    end
end

-- Write to file
local outFile = io.open(outputFile, "w")
if not outFile then
    print("Error: Cannot write to '" .. outputFile .. "'")
    os.exit(1)
end

for _, stmt in ipairs(sqlStatements) do
    outFile:write(stmt .. "\n")
end

outFile:close()

print("Converted " .. #sqlStatements .. " records")
print("Output written to: " .. outputFile)