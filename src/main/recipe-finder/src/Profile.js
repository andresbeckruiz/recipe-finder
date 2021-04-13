import {Alert, Button, Card} from "react-bootstrap";
import {Link, useHistory} from "react-router-dom";
import React, {useState} from "react";
import {useAuth} from "./contexts/AuthContext";

function Profile() {

    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }

    const [error, setError] = useState("")
    const {currentUser, logout} = useAuth()
    const history = useHistory()

    async function handleLogout() {
        setError("")

        try {
            await logout()
            history.push("/login")
        } catch {
            setError("Failed to log out")
        }
    }


    return (
        <div>
            <Link to={"/fridge"}>
                <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Back to Fridge</Button>
            </Link>
            <Card>
                <Card.Body>
                    <h2 className={"text-center mb-4"}>Profile</h2>
                    {error && <Alert variant={"danger"}> {error} </Alert>}
                    <strong>Name: </strong> {/*TODO: Put name here*/}
                    <br/>
                    <strong>Email: </strong> {currentUser.email}
                    <Link to={"/update-password"} className={"btn btn-primary w-100 mt-3"}>
                        Update Password
                    </Link>
                </Card.Body>
            </Card>
            <div className={"w-100 text-center mt-2"}>
                <Button variant={"link"} onClick={handleLogout}> Log Out</Button>
            </div>
        </div>
    );
}

export default Profile;