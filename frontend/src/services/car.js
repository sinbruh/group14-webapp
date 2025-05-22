import { asyncApiRequest } from '@/services/request';

/**
 * Fetches all cars from the backend.
 * @returns {Promise<Array>} A promise that resolves to an array of cars.
 */
export async function fetchAllCars() {
    try {
        return await asyncApiRequest('GET', '/api/cars');
    } catch (error) {
        console.error('Error fetching cars:', error);
        return [];
    }
}

/**
 * Fetches a car by ID from the backend.
 * @param {number} id - The ID of the car to fetch.
 * @returns {Promise<Object|null>} A promise that resolves to the car object or null if not found.
 */
export async function fetchCarById(id) {
    try {
        return await asyncApiRequest('GET', `/api/cars/${id}`);
    } catch (error) {
        console.error(`Error fetching car with ID ${id}:`, error);
        return null;
    }
}

/**
 * Adds a new car to the backend (admin only).
 * @param {Object} car - The car object to add.
 * @returns {Promise<number|null>} A promise that resolves to the ID of the new car or null if failed.
 */
export async function addCar(car) {
    try {
        return await asyncApiRequest('POST', '/api/cars', car);
    } catch (error) {
        console.error('Error adding car:', error);
        throw error;
    }
}

/**
 * Updates a car in the backend (admin only).
 * @param {number} id - The ID of the car to update.
 * @param {Object} car - The updated car object.
 * @returns {Promise<void>} A promise that resolves when the car is updated.
 */
export async function updateCar(id, car) {
    try {
        await asyncApiRequest('PUT', `/api/cars/${id}`, car, true);
    } catch (error) {
        console.error(`Error updating car with ID ${id}:`, error);
        throw error;
    }
}

/**
 * Deletes a car from the backend (admin only).
 * @param {number} id - The ID of the car to delete.
 * @returns {Promise<void>} A promise that resolves when the car is deleted.
 */
export async function deleteCar(id) {
    try {
        await asyncApiRequest('DELETE', `/api/cars/${id}`, null, true);
    } catch (error) {
        console.error(`Error deleting car with ID ${id}:`, error);
        throw error;
    }
}