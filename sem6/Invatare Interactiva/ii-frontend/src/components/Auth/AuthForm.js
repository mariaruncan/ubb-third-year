import React from "react";
import { useState, useContext } from "react";
import { useNavigate } from "react-router";
import AuthContext from "./auth-context";
import "./AuthForm.css";
import axios from "axios";
import { TextField } from "@mui/material";
import { HOST, PORT } from "../../prodURL.js";

const AuthForm = () => {
    const authCtx = useContext(AuthContext);
    const navigate = useNavigate();
    const [isLogin, setIsLogin] = useState(true);
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [email, setEmail] = useState("");
    const [firsName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");


    const clearFields = () => {
        setEmail("");
        setPassword("");
        setLastName("");
        setFirstName("");
        setConfirmPassword("");
    };

    const submitHandler = (event) => {
        event.preventDefault();
        setIsLoading(true);

        if (isLogin) {
            const url = `http://${HOST}:${PORT}/auth/login`;
            const data = {
                email: email,
                password: password
            };

            axios
                .post(url, data)
                .then((resp) => {
                    setIsLoading(false);
                    setIsError(false);
                    setErrorMessage("");

                    const expirationTime = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
                    authCtx.login(resp.data.token, expirationTime.toISOString());

                    navigate("/mainPage");
                })
                .catch((err) => {
                    setIsLoading(false);
                    setIsError(true);
                    setErrorMessage("Login failed!");
                });
        } else {
            const url = `http://${HOST}:${PORT}/auth/register`;
            const data = {
                firstName: firsName,
                lastName: lastName,
                email: email,
                password: password
            };

            axios
                .post(url, data)
                .then((resp) => {
                    setIsLoading(false);
                    setIsError(false);
                    setErrorMessage("");
                    clearFields();
                    setIsLogin(true);
                })
                .catch((err) => {
                    setIsLoading(false);
                    setIsError(true);
                    setErrorMessage("Register failed!");
                });
        }
    };

    const validateEmail = (email) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }

    const handleEmailChanged = (e) => {
        const email = e.target.value;
        setIsError(false);
        setErrorMessage("");
        setEmail(email);

        if (!validateEmail(email)) {
            setIsError(true);
            setErrorMessage("Invalid email format!")
            return;
        }
    }

    const checkPasswords = (confirmPassword) => {
        if (password !== confirmPassword) {
            setIsError(true);
            setErrorMessage("Passwords does not match!");
            return;
        }

        setIsError(false);
        setErrorMessage("");
    };

    return (
        <div className={"authPage"}>
            <section className={isLogin ? "login" : "signUp"}>
                <h2>{isLogin ? "Login" : "Register"}</h2>
                <form onSubmit={submitHandler}>
                    {!isLogin &&
                        <div>
                            <TextField
                                className={"textField"}
                                label="Your FirstName"
                                variant="standard"
                                required
                                value={firsName}
                                onChange={(e) => {
                                    setFirstName(e.target.value);
                                }}
                            ></TextField>
                            <TextField
                                className={"textField"}
                                label="Your LastName"
                                variant="standard"
                                required
                                value={lastName}
                                onChange={(e) => {
                                    setLastName(e.target.value);
                                }}
                            ></TextField>
                        </div>
                    }
                    <TextField
                        className={"textField"}
                        label="Your Email"
                        variant="standard"
                        required
                        value={email}
                        onChange={handleEmailChanged}
                    ></TextField>
                    <TextField
                        type="password"
                        className={"textField"}
                        label="Your Password"
                        variant="standard"
                        required
                        value={password}
                        onChange={(e) => {
                            setPassword(e.target.value);
                        }}
                    ></TextField>
                    {!isLogin &&
                        <TextField
                            type="password"
                            className={"textField"}
                            label="Confirm Password"
                            variant="standard"
                            required
                            value={confirmPassword}
                            onChange={(e) => {
                                setConfirmPassword(e.target.value);
                                checkPasswords(e.target.value);
                            }}
                        ></TextField>
                    }
                    {isError && <div className={"error"}>{errorMessage}</div>}
                    <div className={"actions"}>
                        {!isLoading && (
                            <button type="submit" disabled={isError}>
                                {isLogin ? "Login" : "Create Account"}
                            </button>
                        )}
                    </div>
                </form>
                {isLogin &&
                    <a className="secondButton" onClick={() => {
                        clearFields();
                        setIsError(false);
                        setErrorMessage("");
                        setIsLogin(false)
                    }}>Register</a>
                }
                {!isLogin &&
                    <a className="secondButton" onClick={() => {
                        clearFields();
                        setIsError(false);
                        setErrorMessage("");
                        setIsLogin(true)
                    }}>Login</a>
                }
            </section>
        </div>
    );
};
export default AuthForm;
