import {
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from '@mui/material';
import Customer from './Customer';

const Customers = ({ customers }) => {
    return (
        customers.length === 0 ? <h1>No customers found</h1> :
            <TableContainer component={Paper}>
                <Table aria-label="collapsible table">
                    <TableHead>
                        <TableRow>
                            <TableCell />
                            {/* <TableCell style={{ fontWeight: 600 }}>Customer ID</TableCell> */}
                            <TableCell style={{ fontWeight: 600 }}>Customer Name</TableCell>
                            <TableCell align="right" style={{ fontWeight: 600 }}>Email</TableCell>
                            <TableCell align="right" style={{ fontWeight: 600 }}>Age</TableCell>
                            <TableCell />
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {customers.map((row) => (
                            <Customer key={row.id} {...row} />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
    );
}

export default Customers;