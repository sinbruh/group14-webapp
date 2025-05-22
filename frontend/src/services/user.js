import { asyncApiRequest } from '@/services/request';

/**
 * Fetches all users from the backend (admin only).
 * @returns {Promise<Array>} A promise that resolves to an array of users.
 */
export async function fetchAllUsers() {
    try {
        return await asyncApiRequest('GET', '/api/users');
    } catch (error) {
        console.error('Error fetching users:', error);
        return [];
    }
}

/**
 * Fetches a user by email from the backend.
 * @param {string} email - The email of the user to fetch.
 * @returns {Promise<Object|null>} A promise that resolves to the user object or null if not found.
 */
export async function fetchUserByEmail(email) {
    try {
        return await asyncApiRequest('GET', `/api/users/${email}`);
    } catch (error) {
        console.error(`Error fetching user with email ${email}:`, error);
        return null;
    }
}

/**
 * Updates a user in the backend.
 * @param {string} email - The email of the user to update.
 * @param {Object} userData - The updated user data.
 * @returns {Promise<Object|null>} A promise that resolves to the new JWT token or null if failed.
 */
export async function updateUser(email, userData) {
    try {
        return await asyncApiRequest('PUT', `/api/users/${email}`, userData);
    } catch (error) {
        console.error(`Error updating user with email ${email}:`, error);
        throw error;
    }
}

/**
 * Updates a user's password in the backend.
 * @param {string} email - The email of the user to update the password for.
 * @param {Object} passwordData - The password data containing old and new passwords.
 * @returns {Promise<void>} A promise that resolves when the password is updated.
 */
export async function updateUserPassword(email, passwordData) {
    try {
        await asyncApiRequest('PUT', `/api/users/${email}/password`, passwordData, true);
    } catch (error) {
        console.error(`Error updating password for user with email ${email}:`, error);
        throw error;
    }
}

/**
 * Deletes a user from the backend.
 * @param {string} email - The email of the user to delete.
 * @returns {Promise<void>} A promise that resolves when the user is deleted.
 */
export async function deleteUser(email) {
    try {
        await asyncApiRequest('DELETE', `/api/users/${email}`, null, true);
    } catch (error) {
        console.error(`Error deleting user with email ${email}:`, error);
        throw error;
    }
}