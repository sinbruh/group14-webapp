import Image from "next/image"
import Link from "next/link"
import { Fuel, Settings, Users } from "lucide-react"
import { ChevronRight } from "lucide-react"

import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card"

export function FeaturedCars() {
    const featuredCars = [
        {
            id: 1,
            name: "Toyota Camry",
            category: "Sedan",
            price: 45,
            image: "/placeholder.svg?height=200&width=300",
            features: {
                seats: 5,
                transmission: "Automatic",
                fuel: "Hybrid",
            },
            isNew: true,
        },
        {
            id: 2,
            name: "Honda CR-V",
            category: "SUV",
            price: 65,
            image: "/placeholder.svg?height=200&width=300",
            features: {
                seats: 5,
                transmission: "Automatic",
                fuel: "Gasoline",
            },
            isNew: false,
        },
        {
            id: 3,
            name: "BMW 3 Series",
            category: "Luxury",
            price: 95,
            image: "/placeholder.svg?height=200&width=300",
            features: {
                seats: 5,
                transmission: "Automatic",
                fuel: "Gasoline",
            },
            isNew: false,
        },
        {
            id: 4,
            name: "Tesla Model 3",
            category: "Electric",
            price: 85,
            image: "/placeholder.svg?height=200&width=300",
            features: {
                seats: 5,
                transmission: "Automatic",
                fuel: "Electric",
            },
            isNew: true,
        },
    ]

    return (
        <section className="w-full py-12 md:py-24 lg:py-32">
            <div className="container px-4 md:px-6">
                <div className="flex flex-col items-center justify-center space-y-4 text-center">
                    <div className="space-y-2">
                        <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">Featured Vehicles</h2>
                        <p className="max-w-[900px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
                            Explore our selection of premium vehicles for your next adventure.
                        </p>
                    </div>
                </div>
                <div className="mx-auto grid max-w-5xl items-center gap-6 py-12 md:grid-cols-2 lg:grid-cols-4">
                    {featuredCars.map((car) => (
                        <Card key={car.id} className="overflow-hidden">
                            <CardHeader className="p-0">
                                <div className="relative">
                                    <Image
                                        src={car.image || "/placeholder.svg"}
                                        alt={car.name}
                                        width={300}
                                        height={200}
                                        className="w-full object-cover"
                                    />
                                    {car.isNew && <Badge className="absolute top-2 right-2 bg-emerald-600">New</Badge>}
                                </div>
                            </CardHeader>
                            <CardContent className="p-4">
                                <div className="flex justify-between items-start">
                                    <div>
                                        <h3 className="font-bold">{car.name}</h3>
                                        <p className="text-sm text-gray-500">{car.category}</p>
                                    </div>
                                    <div className="text-right">
                                        <span className="font-bold text-lg">${car.price}</span>
                                        <p className="text-xs text-gray-500">per day</p>
                                    </div>
                                </div>
                                <div className="grid grid-cols-3 gap-2 mt-4 text-sm">
                                    <div className="flex flex-col items-center gap-1">
                                        <Users className="h-4 w-4 text-gray-500" />
                                        <span>{car.features.seats} Seats</span>
                                    </div>
                                    <div className="flex flex-col items-center gap-1">
                                        <Settings className="h-4 w-4 text-gray-500" />
                                        <span>{car.features.transmission}</span>
                                    </div>
                                    <div className="flex flex-col items-center gap-1">
                                        <Fuel className="h-4 w-4 text-gray-500" />
                                        <span>{car.features.fuel}</span>
                                    </div>
                                </div>
                            </CardContent>
                            <CardFooter className="p-4 pt-0">
                                <Button asChild className="w-full bg-emerald-600 hover:bg-emerald-700">
                                    <Link href={`/cars/${car.id}`}>View Details</Link>
                                </Button>
                            </CardFooter>
                        </Card>
                    ))}
                </div>
                <div className="flex justify-center">
                    <Button asChild variant="outline" size="lg">
                        <Link href="/cars" className="gap-1">
                            View All Cars
                            <ChevronRight className="h-4 w-4" />
                        </Link>
                    </Button>
                </div>
            </div>
        </section>
    )
}
