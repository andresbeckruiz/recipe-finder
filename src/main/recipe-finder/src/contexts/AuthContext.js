import React, {useContext, useState, useEffect} from 'react'
import {auth} from "../firebase"

const AuthContext = React.createContext()

export function useAuth() {
    return useContext(AuthContext)
}

export function AuthProvider({children}){

    const [currentUser, setCurrentUser] = useState()
    //by default we are loading
    const [loading, setLoading] = useState(true)

    function signup(email, password) {
        return auth.createUserWithEmailAndPassword(email,password)
    }

    //only want to call once so we use useEffect here
    useEffect(() => {
        const unsubscribe = auth.onAuthStateChanged(user => {
            //did verification to see if there is a user, so set to false
            setCurrentUser(user)
            setLoading(false)
        })
        //unsubscribes us whenever we unmount this component
        return unsubscribe
    }, [])

    const value = {
        currentUser,
        signup
    }

    return(
        <AuthContext.Provider value={value}>
            {/*only want to render children if we are not loading */}
            {!loading && children}
        </AuthContext.Provider>
    )
}