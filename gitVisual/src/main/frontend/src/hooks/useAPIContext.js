import React, { useContext } from 'react'
import APIContext from '../components/APIProvider'

export default function useAPIContext() {
    const context = useContext(APIContext)
    if (!context) {
        Error('Context is null or someshit')
    }
    return context
}