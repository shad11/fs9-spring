import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    useMediaQuery,
    useTheme
} from '@mui/material';
import { Fragment, useState } from 'react';

export default function ResponsiveDialog({ btnName, btnProps, title, text, action }) {
    const [open, setOpen] = useState(false);
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));

    const handleClickOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    return (
        <Fragment>
            <Button {...btnProps} onClick={handleClickOpen}>
                {btnName}
            </Button>

            <Dialog
                fullScreen={fullScreen}
                open={open}
                onClose={handleClose}
                aria-labelledby="responsive-dialog-title"
            >
                <DialogTitle id="responsive-dialog-title">{title}</DialogTitle>
                <DialogContent>
                    <DialogContentText>{text}</DialogContentText>
                    <DialogActions>
                        <Button autoFocus onClick={action}>
                            Yes
                        </Button>
                        <Button onClick={handleClose} autoFocus>
                            No
                        </Button>
                    </DialogActions>
                </DialogContent>
            </Dialog>
        </Fragment>
    )
}