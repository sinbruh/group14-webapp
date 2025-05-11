"use client";
import Image from "next/image";
import { Check, Fuel, MapPin, Settings, Star, Users } from "lucide-react";
import * as React from "react";

import { Button } from "@/components/ui/button";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Separator } from "@/components/ui/separator";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { DatePickerWithRange } from "@/components/date-range-picker";

/**
 * @typedef {Object} Car
 * @property {number|string} id
 * @property {string} name
 * @property {string} category
 * @property {number} price
 * @property {string} image
 * @property {Object} features
 * @property {number} features.seats
 * @property {string} features.transmission
 * @property {string} features.fuel
 * @property {boolean} [isNew]
 */

/**
 * @param {{
 *   car: Car,
 *   isOpen: boolean,
 *   onClose: () => void
 * }} props
 */
export function CarModal({ car, isOpen, onClose }) {
    const carDetails = {
        ...car,
        deposit: 200,
        rating: 4.8,
        reviews: 124,
        location: "New York City",
        description:
            "This comfortable and reliable vehicle is perfect for city driving and longer trips. This model features excellent fuel efficiency, spacious interiors, and modern amenities to ensure a pleasant driving experience.",
        features: [
            "Automatic Transmission",
            "Bluetooth Connectivity",
            "Backup Camera",
            "Cruise Control",
            "USB Charging Ports",
            "Apple CarPlay & Android Auto",
            "Keyless Entry",
            "Dual-Zone Climate Control",
        ],
        specifications: {
            seats: car.features.seats,
            doors: 4,
            transmission: car.features.transmission,
            fuel: car.features.fuel,
            mileage: "Unlimited",
            luggage: "3 Large Bags",
            year: 2023,
        },
        images: [
            car.image,
            "/placeholder.svg?height=400&width=600",
            "/placeholder.svg?height=400&width=600",
            "/placeholder.svg?height=400&width=600",
            "/placeholder.svg?height=400&width=600",
        ],
        similar: [
            {
                id: 2,
                name: "Honda Accord",
                category: "Sedan",
                price: 48,
                image: "/placeholder.svg?height=200&width=300",
            },
            {
                id: 3,
                name: "Nissan Altima",
                category: "Sedan",
                price: 42,
                image: "/placeholder.svg?height=200&width=300",
            },
            {
                id: 4,
                name: "Hyundai Sonata",
                category: "Sedan",
                price: 40,
                image: "/placeholder.svg?height=200&width=300",
            },
        ],
        providers: [
            {
                id: 1,
                name: "EasyCar Rentals",
                locations: ["New York City", "Los Angeles", "Chicago"],
                priceMultiplier: 1.0,
            },
            {
                id: 2,
                name: "Premium Auto",
                locations: ["New York City", "Miami", "San Francisco"],
                priceMultiplier: 1.2,
            },
            {
                id: 3,
                name: "Budget Wheels",
                locations: ["Chicago", "Dallas", "Atlanta"],
                priceMultiplier: 0.85,
            },
            {
                id: 4,
                name: "Luxury Drive",
                locations: ["New York City", "Los Angeles", "Las Vegas"],
                priceMultiplier: 1.5,
            },
        ],
    };

    const [selectedProvider, setSelectedProvider] = React.useState(1);
    const [adjustedPrice, setAdjustedPrice] = React.useState(carDetails.price);

    const provider = carDetails.providers.find((p) => p.id === selectedProvider);

    React.useEffect(() => {
        if (provider) {
            setAdjustedPrice(Math.round(carDetails.price * provider.priceMultiplier));
        }
    }, [selectedProvider, carDetails.price, provider]);

    return (
        <Dialog open={isOpen} onOpenChange={(open) => !open && onClose()}>
            <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto p-0">
                <div className="p-6">
                    <h1 className="text-2xl font-bold mb-2">{carDetails.name}</h1>
                    <div className="flex flex-wrap items-center gap-x-4 gap-y-2 mb-6">
                        <div className="flex items-center">
                            <Star className="h-4 w-4 text-yellow-400 fill-yellow-400" />
                            <span className="ml-1 font-medium">{carDetails.rating}</span>
                            <span className="ml-1 text-gray-500">({carDetails.reviews} reviews)</span>
                        </div>
                        <div className="flex items-center">
                            <MapPin className="h-4 w-4 text-gray-500" />
                            <span className="ml-1">{carDetails.location}</span>
                        </div>
                        <div className="flex items-center">
                            <Users className="h-4 w-4 text-gray-500" />
                            <span className="ml-1">{carDetails.specifications.seats} Seats</span>
                        </div>
                        <div className="flex items-center">
                            <Settings className="h-4 w-4 text-gray-500" />
                            <span className="ml-1">{carDetails.specifications.transmission}</span>
                        </div>
                        <div className="flex items-center">
                            <Fuel className="h-4 w-4 text-gray-500" />
                            <span className="ml-1">{carDetails.specifications.fuel}</span>
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                        <Image
                            src={carDetails.images[0] || "/placeholder.svg"}
                            alt={carDetails.name}
                            width={600}
                            height={400}
                            className="w-full h-64 object-cover rounded-lg"
                        />
                        <div className="grid grid-cols-2 gap-2">
                            {carDetails.images.slice(1, 5).map((image, index) => (
                                <Image
                                    key={index}
                                    src={image || "/placeholder.svg"}
                                    alt={`${carDetails.name} view ${index + 2}`}
                                    width={300}
                                    height={200}
                                    className="w-full h-[120px] object-cover rounded-lg"
                                />
                            ))}
                        </div>
                    </div>

                    <div className="grid gap-6 lg:grid-cols-[1fr_300px]">
                        <div>
                            <Tabs defaultValue="description" className="mb-6">
                                <TabsList className="w-full justify-start border-b rounded-none h-auto p-0 bg-transparent">
                                    <TabsTrigger
                                        value="description"
                                        className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-emerald-600 data-[state=active]:shadow-none bg-transparent px-4 py-2"
                                    >
                                        Description
                                    </TabsTrigger>
                                    <TabsTrigger
                                        value="features"
                                        className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-emerald-600 data-[state=active]:shadow-none bg-transparent px-4 py-2"
                                    >
                                        Features
                                    </TabsTrigger>
                                    <TabsTrigger
                                        value="specifications"
                                        className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-emerald-600 data-[state=active]:shadow-none bg-transparent px-4 py-2"
                                    >
                                        Specifications
                                    </TabsTrigger>
                                </TabsList>
                                <TabsContent value="description" className="pt-4">
                                    <p className="text-gray-700">{carDetails.description}</p>
                                </TabsContent>
                                <TabsContent value="features" className="pt-4">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
                                        {carDetails.features.map((feature, index) => (
                                            <div key={index} className="flex items-center gap-2">
                                                <Check className="h-4 w-4 text-emerald-600" />
                                                <span>{feature}</span>
                                            </div>
                                        ))}
                                    </div>
                                </TabsContent>
                                <TabsContent value="specifications" className="pt-4">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Seats</span>
                                            <span className="font-medium">{carDetails.specifications.seats}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Doors</span>
                                            <span className="font-medium">{carDetails.specifications.doors}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Transmission</span>
                                            <span className="font-medium">{carDetails.specifications.transmission}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Fuel Type</span>
                                            <span className="font-medium">{carDetails.specifications.fuel}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Mileage</span>
                                            <span className="font-medium">{carDetails.specifications.mileage}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Luggage</span>
                                            <span className="font-medium">{carDetails.specifications.luggage}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Year</span>
                                            <span className="font-medium">{carDetails.specifications.year}</span>
                                        </div>
                                    </div>
                                </TabsContent>
                            </Tabs>
                        </div>

                        <div className="border rounded-lg overflow-hidden">
                            <div className="bg-emerald-50 p-4 border-b">
                                <div className="flex justify-between items-center">
                                    <div>
                                        <span className="text-2xl font-bold">${adjustedPrice}</span>
                                        <span className="text-gray-500"> / day</span>
                                    </div>
                                    <div className="flex items-center">
                                        <Star className="h-4 w-4 text-yellow-400 fill-yellow-400" />
                                        <span className="ml-1 font-medium">{carDetails.rating}</span>
                                    </div>
                                </div>
                            </div>
                            <div className="p-4">
                                <form className="space-y-4">
                                    <div className="space-y-2">
                                        <label className="text-sm font-medium">Pick-up & Return Date</label>
                                        <DatePickerWithRange className="w-full" />
                                    </div>
                                    <div className="space-y-4">
                                        <div className="space-y-2">
                                            <label className="text-sm font-medium">Provider</label>
                                            <div className="flex items-center gap-2 rounded-md border p-2">
                                                <Settings className="h-4 w-4 text-gray-500" />
                                                <Select
                                                    value={selectedProvider.toString()}
                                                    onValueChange={(value) => setSelectedProvider(Number(value))}
                                                >
                                                    <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                                        <SelectValue placeholder="Select provider" />
                                                    </SelectTrigger>
                                                    <SelectContent>
                                                        {carDetails.providers.map((provider) => (
                                                            <SelectItem key={provider.id} value={provider.id.toString()}>
                                                                {provider.name} (
                                                                {provider.priceMultiplier === 1
                                                                    ? "Standard"
                                                                    : provider.priceMultiplier < 1
                                                                        ? "Economy"
                                                                        : "Premium"}
                                                                )
                                                            </SelectItem>
                                                        ))}
                                                    </SelectContent>
                                                </Select>
                                            </div>
                                        </div>

                                        <div className="space-y-2">
                                            <label className="text-sm font-medium">Pick-up Location</label>
                                            <div className="flex items-center gap-2 rounded-md border p-2">
                                                <MapPin className="h-4 w-4 text-gray-500" />
                                                <Select>
                                                    <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                                        <SelectValue placeholder="Select location" />
                                                    </SelectTrigger>
                                                    <SelectContent>
                                                        {provider?.locations.map((location) => (
                                                            <SelectItem key={location} value={location}>
                                                                {location}
                                                            </SelectItem>
                                                        ))}
                                                    </SelectContent>
                                                </Select>
                                            </div>
                                        </div>
                                    </div>
                                    <Separator />
                                    <div className="space-y-2">
                                        <div className="flex justify-between">
                                            <span>Base Rate (3 days)</span>
                                            <span>${adjustedPrice * 3}</span>
                                        </div>
                                        <div className="flex justify-between text-gray-500">
                                            <span>Taxes & Fees</span>
                                            <span>${Math.round(adjustedPrice * 3 * 0.15)}</span>
                                        </div>
                                        <div className="flex justify-between text-gray-500">
                                            <span>Security Deposit (refundable)</span>
                                            <span>${carDetails.deposit}</span>
                                        </div>
                                        <div className="flex justify-between font-bold text-lg pt-2">
                                            <span>Total</span>
                                            <span>${adjustedPrice * 3 + Math.round(adjustedPrice * 3 * 0.15)}</span>
                                        </div>
                                    </div>
                                    <Button className="w-full bg-emerald-600 hover:bg-emerald-700">Book Now</Button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
}