import { Delete as DeleteIcon } from '@mui/icons-material';
import { TableCell, TableRow } from '@mui/material';
import { connect } from 'react-redux';
import { deleteAccount } from '../../store/operations';
import SimpleDialogButton from '../Common/SimpleDialogButton';

const Account = (props) => {
    const { id, number, currency, balance, customerId, deleteAccount } = props;

    return (
        <TableRow>
            {/* <TableCell component="th" scope="row">{id}</TableCell> */}
            <TableCell component="th" scope="row">{number}</TableCell>
            <TableCell>{currency}</TableCell>
            <TableCell align="right">{balance}</TableCell>
            <TableCell>
                <SimpleDialogButton
                    btnName="Delete"
                    btnProps={{ variant: "outlined", color: "error", size: "small", endIcon: <DeleteIcon /> }}
                    title="Delete Account"
                    text="Are you sure you want to delete this account?"
                    action={() => deleteAccount(customerId, id)}
                />
            </TableCell>
        </TableRow>
    );
}

const mapDispatchToProps = dispatch => ({
    deleteAccount: (customerId, accountId) => dispatch(deleteAccount(customerId, accountId))
});

export default connect(null, mapDispatchToProps)(Account);