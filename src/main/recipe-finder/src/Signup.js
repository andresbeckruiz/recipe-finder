import React, {useRef, useState} from 'react'
import {Form, Button, Card, Alert} from 'react-bootstrap'
import {useAuth} from "./contexts/AuthContext";
import {Link, useHistory} from "react-router-dom";

export default function Signup() {
    //const nameRef = useRef()
    const emailRef = useRef()
    const passwordRef = useRef()
    const passwordConfirmRef = useRef()
    const {signup} = useAuth()
    const [error, setError] = useState("")
    const [loading, setLoading] = useState(false)
    const history = useHistory()

    //here is where we can check that fields are in the right format that we want
    async function handleSubmit(e) {
        e.preventDefault()

        if (passwordRef.current.value !== passwordConfirmRef.current.value) {
            //return here beause we don't want to continue with signup, exit function
            return setError("Passwords do not match")
        }
        try {
            setError('')
            //don't want user to click sign up button multiple times
            setLoading(true)
            await signup(emailRef.current.value, passwordRef.current.value)
            history.push("/")
        } catch (error) {
            setError("Failed to create an account")
            console.log(error)
        }
        setLoading(false)
    }

    return(
        <>
          <Card>
              <Card.Body>
                <h2 className={"text-center mb-3"}> Sign Up</h2>
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
                    <Form.Group id="password-confirm">
                        <Form.Label>Password Confirmation</Form.Label>
                        <Form.Control type={"password"} ref={passwordConfirmRef} required/>
                    </Form.Group>
                    <Button disabled={loading} className={"w-100"} type={"submit"}>
                        Sign Up
                    </Button>
                </Form>
              </Card.Body>
          </Card>
          <div className={"w-100 text-center mt-2"}>
              Already have an account? <Link to={"/login"}>Log In</Link>
          </div>
        </>
    )
}