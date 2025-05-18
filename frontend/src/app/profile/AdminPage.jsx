import React, { useEffect, useState } from 'react';
import { asyncApiRequest } from '@/services/request';
import { isAdmin, isLoggedIn } from '@/services/authentication';

export default function AdminPage() {
    const [users, setUsers] = useState([]);
    const [cars, setCars] = useState([]);

    useEffect(() => {
        if (isLoggedIn() && isAdmin()) {
            fetchUsers();
            fetchCars();
        }
    }, []);

    async function fetchUsers() {
        try {
            const data = await asyncApiRequest('GET', '/api/admin/users');
            setUsers(data);
        } catch (error) {
            console.error(error);
        }
    }

    async function fetchCars() {
        try {
            const data = await asyncApiRequest('GET', '/api/configurations');
            setCars(data);
        } catch (error) {
            console.error(error);
        }
    }

    async function handleDeleteUser(userId) {
        try {
            await asyncApiRequest('DELETE', `/api/admin/users/${userId}`);
            await fetchUsers();
        } catch (error) {
            console.error(error);
        }
    }

    async function handleDeleteCar(carId) {
        try {
            await asyncApiRequest('DELETE', `/api/admin/cars/${carId}`);
            await fetchCars();
        } catch (error) {
            console.error(error);
        }
    }

    if (!isLoggedIn()) {
        return <div>Please log in first.</div>;
    }

    if (!isAdmin()) {
        return <div>You do not have permission to view this page.</div>;
    }

    return (
        <div style={{ margin: '2rem auto', maxWidth: '900px', padding: '1rem' }}>
            <h1>Admin Dashboard</h1>
            <h2>Users</h2>
            <table>
                <tbody>
                {users.map(u => (
                    <tr key={u.id}>
                        <td>{u.email}</td>
                        <td>
                            <button onClick={() => handleDeleteUser(u.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <h2>Cars</h2>
            <table>
                <tbody>
                {cars.map(c => (
                    <tr key={c.id}>
                        <td>{c.make} {c.model}</td>
                        <td>
                            <button>Edit</button>
                            <button>Hide</button>
                            <button onClick={() => handleDeleteCar(c.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}