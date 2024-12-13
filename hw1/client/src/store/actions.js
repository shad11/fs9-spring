// Used to define the actions that will be dispatched to the reducer
// The actions are defined as functions that return an object with a type and a payload
// Are used in operations for manipulating data using API calls
import * as types from './types';

export const customersLoading = (isLoading) => ({
    type: types.FETCH_CUSTOMERS_LOADING,
    payload: isLoading
});

export const customersSaving = (data) => ({
    type: types.FETCH_CUSTOMERS_SUCCESS,
    payload: data
});

export const getError = (error) => ({
    type: types.GET_ERROR,
    payload: error
});