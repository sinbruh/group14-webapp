import { asyncApiRequest } from '@/services/request';

/**
 * Fetches all rentals from the backend (admin only).
 * @returns {Promise<Array>} A promise that resolves to an array of rentals.
 */
export async function fetchAllRentals() {
    try {
        return await asyncApiRequest('GET', '/api/rentals');
    } catch (error) {
        console.error('Error fetching rentals:', error);
        return [];
    }
}

/**
 * Fetches a rental by ID from the backend.
 * @param {number} id - The ID of the rental to fetch.
 * @returns {Promise<Object|null>} A promise that resolves to the rental object or null if not found.
 */
export async function fetchRentalById(id) {
    try {
        return await asyncApiRequest('GET', `/api/rentals/${id}`);
    } catch (error) {
        console.error(`Error fetching rental with ID ${id}:`, error);
        return null;
    }
}

/**
 * Adds a new rental for a user and provider in the backend.
 * @param {string} email - The email of the user to add the rental for.
 * @param {number} providerId - The ID of the provider to add the rental for.
 * @param {Object} rental - The rental object to add.
 * @returns {Promise<number|null>} A promise that resolves to the ID of the new rental or null if failed.
 */
export async function addRental(email, providerId, rental) {
    try {
        return await asyncApiRequest('POST', `/api/rentals/${email}/${providerId}`, rental);
    } catch (error) {
        console.error('Error adding rental:', error);
        throw error;
    }
}

/**
 * Deletes a rental from the backend.
 * @param {number} id - The ID of the rental to delete.
 * @returns {Promise<void>} A promise that resolves when the rental is deleted.
 */
export async function deleteRental(id) {
    try {
        await asyncApiRequest('DELETE', `/api/rentals/${id}`, null, true);
    } catch (error) {
        console.error(`Error deleting rental with ID ${id}:`, error);
        throw error;
    }
}