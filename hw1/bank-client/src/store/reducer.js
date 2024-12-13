// Reducer changes the state of the application based on the action type
import * as types from './types';

const initialState = {
    isLoading: false,
    customers: [],
    error: null
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case types.FETCH_CUSTOMERS_LOADING:
            return {
                ...state,
                isLoading: action.payload
            };
        case types.FETCH_CUSTOMERS_SUCCESS:
            return {
                ...state,
                customers: action.payload
            };
        case types.GET_ERROR:
            return {
                ...state,
                error: action.payload
            };
        default:
            return state;
    }
};

export default reducer;