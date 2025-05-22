import { asyncApiRequest } from '@/services/request';

/**
 * Fetches all receipts from the backend (admin only).
 * @returns {Promise<Array>} A promise that resolves to an array of receipts.
 */
export async function fetchAllReceipts() {
    try {
        return await asyncApiRequest('GET', '/api/receipts');
    } catch (error) {
        console.error('Error fetching receipts:', error);
        return [];
    }
}

/**
 * Fetches a receipt by ID from the backend.
 * @param {number} id - The ID of the receipt to fetch.
 * @returns {Promise<Object|null>} A promise that resolves to the receipt object or null if not found.
 */
export async function fetchReceiptById(id) {
    try {
        return await asyncApiRequest('GET', `/api/receipts/${id}`);
    } catch (error) {
        console.error(`Error fetching receipt with ID ${id}:`, error);
        return null;
    }
}

/**
 * Adds a new receipt for a rental in the backend.
 * @param {string} email - The email of the user to add the receipt for.
 * @param {number} rentalId - The ID of the rental to generate the receipt from.
 * @param {number} totalPrice - The total price of the receipt.
 * @returns {Promise<number|null>} A promise that resolves to the ID of the new receipt or null if failed.
 */
export async function addReceipt(email, rentalId, totalPrice) {
    try {
        return await asyncApiRequest('POST', `/api/receipts/${email}/${rentalId}`, totalPrice);
    } catch (error) {
        console.error('Error adding receipt:', error);
        throw error;
    }
}

/**
 * Deletes a receipt from the backend.
 * @param {number} id - The ID of the receipt to delete.
 * @returns {Promise<void>} A promise that resolves when the receipt is deleted.
 */
export async function deleteReceipt(id) {
    try {
        await asyncApiRequest('DELETE', `/api/receipts/${id}`, null, true);
    } catch (error) {
        console.error(`Error deleting receipt with ID ${id}:`, error);
        throw error;
    }
}