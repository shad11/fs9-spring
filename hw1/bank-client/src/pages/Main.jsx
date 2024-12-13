import { useCallback, useEffect } from 'react';
import { connect } from 'react-redux';
import Customers from '../components/Customers';
import Loader from '../components/Loader';
import { getCustomers } from '../store/operations';

const Main = ({ isLoading, customers, load }) => {
    const initFetch = useCallback(() => {
        load()
    }, [load]);

    useEffect(() => {
        initFetch();
    }, [initFetch]);

    return (
        isLoading ? <Loader /> : <Customers customers={customers} />
    );
}

const mapStateToProps = ({ isLoading, customers }) => ({
    isLoading: isLoading,
    customers: customers,
});

const mapDispatchToProps = (dispatch) => ({
    load: () => dispatch(getCustomers())
});

export default connect(mapStateToProps, mapDispatchToProps)(Main);