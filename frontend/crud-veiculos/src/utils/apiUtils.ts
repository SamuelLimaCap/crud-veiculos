interface PageInfo {
  pageNum: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

interface ApiResponse {
  status: string;
  message: string;
  content: any[];
  pageInfo: PageInfo;
}

interface State {
  items: any[];
  pageInfo: PageInfo;
  loading: boolean;
  error: string | null;
}

type Action = 
  | { type: 'FETCH_START' }
  | { type: 'FETCH_SUCCESS'; payload: ApiResponse }
  | { type: 'APPEND_SUCCESS'; payload: ApiResponse }
  | { type: 'FETCH_ERROR'; payload: string };

const initialState: State = {
  items: [],
  pageInfo: {
    pageNum: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
  },
  loading: false,
  error: null,
};

const reducer = (state: State, action: Action): State => {
  switch (action.type) {
    case 'FETCH_START':
      return { ...state, loading: true, error: null };
    
    case 'FETCH_SUCCESS':
      return {
        ...state,
        items: action.payload.content,
        pageInfo: action.payload.pageInfo,
        loading: false,
      };
    
    case 'APPEND_SUCCESS':
      return {
        ...state,
        items: [...state.items, ...action.payload.content],
        pageInfo: action.payload.pageInfo,
        loading: false,
      };
    
    case 'FETCH_ERROR':
      return { ...state, error: action.payload, loading: false };
    
    default:
      return state;
  }
};
