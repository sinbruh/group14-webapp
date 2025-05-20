import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

export function flattenCars(cars) {
     return cars.flatMap(car =>
        car.configurations.map(configuration => {
            const { configurations, ...rest } = car;
            return {
                ...rest,
                configuration
            };
        })
    );
}