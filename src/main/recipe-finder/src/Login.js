import React, {useRef, useState} from 'react'
import {Form, Button, Card, Alert} from 'react-bootstrap'
import {useAuth} from "./contexts/AuthContext"
import {Link, useHistory} from "react-router-dom"
import axios from "axios";

export default function Login() {
    //const nameRef = useRef()
    const emailRef = useRef()
    const passwordRef = useRef()
    const {login} = useAuth()
    const [error, setError] = useState("")
    const [loading, setLoading] = useState(false)
    const history = useHistory()

    const createUser = (email) => {
        console.log(email)
        const toSend = {
            name: email
        };
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        // sending this information to backend
        axios.post(
            "http://localhost:4567/newUser",
            toSend,
            config
        ).then((response) => {
            console.log(response)
            history.push("/fridge")
        })
            .catch(function (error) {
                console.log(error);
            });
    }

    //here is where we can check that fields are in the right format that we want
    async function handleSubmit(e) {
        e.preventDefault()

        try {
            setError('')
            //don't want user to click login button multiple times
            setLoading(true)
            await login(emailRef.current.value, passwordRef.current.value)
            await createUser(emailRef.current.value)
        } catch (error) {
            setError("Failed to log in")
            console.log(error)
        }
        setLoading(false)
    }

    return(
        <>
            <Card>
                <Card.Body>
                    <h2 className={"text-center mb-3"}> Log In</h2>
                    {error && <Alert variant={"danger"}> {error} </Alert>}
                    <Form onSubmit={handleSubmit}>
                        {/*<Form.Group id="name">*/}
                        {/*    <Form.Label>Name</Form.Label>*/}
                        {/*    <Form.Control type={"name"} ref={{nameRef}} required/>*/}
                        {/*</Form.Group>*/}
                        <Form.Group id="email">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type={"email"} ref={emailRef} required/>
                        </Form.Group>
                        <Form.Group id="password">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type={"password"} ref={passwordRef} required/>
                        </Form.Group>
                        <Button disabled={loading} className={"w-100"} type={"submit"}>
                            Log In
                        </Button>
                    </Form>
                    <div className={"w-100 text-center mt-3"}>
                        <Link to={"/forgot-password"}>Forgot Password?</Link>
                    </div>
                </Card.Body>
            </Card>
            <div className={"w-100 text-center mt-2"}>
                Need an account? <Link to={"/signup"}>Sign Up</Link>
            </div>
        </>
    )
}