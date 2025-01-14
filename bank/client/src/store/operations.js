//Operations for fetching users from API
import { customersLoading, customersSaving, getError } from './actions';

export const getCustomers = () => dispatch => {
    dispatch(customersLoading(true));

    // fetch('https://jsonplaceholder.typicode.com/users')
    // TODO: Replace fetch with real API call to get customers
    // Send a GET request to the server to fetch customers
    fetch('/data/data.json')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch customers');
            }

            return response.json();
        })
        .then(data => {
            // console.log(data);
            dispatch(customersSaving(data));
        })
        .catch(error => {
            dispatch(getError(error.message));
        });

    dispatch(customersLoading(false));
};

export const deleteCustomer = (id) => (dispatch, getState) => {
    // TODO: Replace fetch with real API call to delete a customer
    // Send a DELETE request to the server with the customer id
    fetch('/data/data.json')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete a customer with id: ' + id);
            }

            const { customers } = getState();
            // console.log(id);
            const updatedCustomers = customers.filter(customer => customer.id !== id);

            dispatch(customersSaving(updatedCustomers));
        })
        .catch(error => {
            dispatch(getError(error.message));
        });
}

export const deleteAccount = (customerId, accountId) => (dispatch, getState) => {
    // TODO: Replace fetch with real API call to delete an account
    // Send a DELETE request to the server with the customer id and account id
    fetch('/data/data.json')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete an account with id: ' + accountId);
            }

            const { customers } = getState();
            const updatedCustomers = customers.map(customer => {
                if (customer.id === customerId) {
                    const updatedAccounts = customer.accounts.filter(account => account.id !== accountId);
                    return { ...customer, accounts: updatedAccounts };
                }

                return customer;
            });

            dispatch(customersSaving(updatedCustomers));
        })
        .catch(error => {
            dispatch(getError(error.message));
        });
}
