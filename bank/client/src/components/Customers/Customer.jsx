import {
    Delete as DeleteIcon,
    KeyboardArrowDown,
    KeyboardArrowUp
} from '@mui/icons-material';
import {
    Box,
    Collapse,
    IconButton,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow
} from '@mui/material';
import { Fragment, useState } from "react";
import { connect } from 'react-redux';
import { deleteCustomer } from '../../store/operations';
import SimpleDialogButton from '../Common/SimpleDialogButton';
import Account from './Account';

const Customer = (props) => {
    const { id, name, email, age, accounts, deleteCustomer } = props;
    const [open, setOpen] = useState(false);

    return (
        <Fragment>
            <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
                <TableCell>
                    {accounts.length > 0 &&
                        <IconButton
                            aria-label="expand row"
                            size="small"
                            onClick={() => setOpen(!open)}
                        >
                            {open ? <KeyboardArrowUp /> : <KeyboardArrowDown />}
                        </IconButton>
                    }
                </TableCell>
                {/* <TableCell component="th" scope="row">{id}</TableCell> */}
                <TableCell component="th" scope="row">{name}</TableCell>
                <TableCell align="right">{email}</TableCell>
                <TableCell align="right">{age}</TableCell>
                <TableCell>
                    {/* <Button variant="outlined" startIcon={<DeleteIcon />} color="error"
                        onClick={() => deleteCustomer(id)}
                    >
                        Delete
                    </Button> */}
                    <SimpleDialogButton
                        btnName="Delete"
                        btnProps={{ variant: "outlined", color: "error", startIcon: <DeleteIcon /> }}
                        title="Delete Customer"
                        text="Are you sure you want to delete this customer?"
                        action={() => deleteCustomer(id)}
                    />
                </TableCell>
            </TableRow>

            <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                    <Collapse in={open} timeout="auto" unmountOnExit>
                        <Box sx={{ margin: 1 }}>
                            {/* <Typography variant="h6" gutterBottom component="div">
                                Accounts
                            </Typography> */}
                            {accounts.length > 0 &&
                                <Table size="small" aria-label="purchases">
                                    <TableHead>
                                        <TableRow>
                                            {/* <TableCell style={{ fontWeight: 600 }}>Account ID</TableCell> */}
                                            <TableCell style={{ fontWeight: 600 }}>Account Number</TableCell>
                                            <TableCell style={{ fontWeight: 600 }}>Currency</TableCell>
                                            <TableCell align="right" style={{ fontWeight: 600 }}>Balance</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {accounts.map((account) => (
                                            <Account key={account.id} customerId={id} {...account} />
                                        ))}
                                    </TableBody>
                                </Table>
                            }
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </Fragment>
    );
}

const mapDispatchToProps = dispatch => ({
    deleteCustomer: (id) => dispatch(deleteCustomer(id))
});

export default connect(null, mapDispatchToProps)(Customer);