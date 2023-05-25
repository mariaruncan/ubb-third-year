import React, { useContext, useState } from 'react';
import './Navbar.css';
import AuthContext from "../Auth/auth-context";
import { IconButton, Menu, MenuItem } from '@mui/material';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import { Settings, SettingsInputComponentSharp } from '@material-ui/icons';

export const Navbar = () => {
    const authCtx = useContext(AuthContext);
    const user = JSON.parse(localStorage.getItem('user'));

    const [anchorEl, setAnchorEl] = useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <div className="Navbar">
            <div className="Navbar-left">
                <p>MENIU</p>
            </div>
            <div className="Navbar-center">
                <p className="Navbar-title" >nume utilizator</p>
            </div>
            <div className="Navbar-right">
                <IconButton className="Navbar-item" onClick={handleClick}>
                    <Settings />
                    <ArrowDropDownIcon />
                </IconButton>
                <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleClose}>
                    <MenuItem onClick={() => { authCtx.logout() }}>Logout</MenuItem>
                </Menu>
            </div>
        </div>
    );
};