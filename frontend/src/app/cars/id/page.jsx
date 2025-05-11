import Link from "next/link"
import Image from "next/image"
import { ArrowLeft, Car, Check, ChevronRight, Fuel, MapPin, Settings, Star, Users } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { DatePickerWithRange } from "@/components/date-range-picker"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Separator } from "@/components/ui/separator"

export default function CarDetailPage({ params}) {
    const car = {
        id: params.id,
        name: "Toyota Camry",
        category: "Sedan",
        price: 45,
        deposit: 200,
        rating: 4.8,
        reviews: 124,
        location: "New York City",
        description:
            "The Toyota Camry is a comfortable and reliable sedan, perfect for city driving and longer trips. This model features excellent fuel efficiency, spacious interiors, and modern amenities to ensure a pleasant driving experience.",
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
            seats: 5,
            doors: 4,
            transmission: "Automatic",
            fuel: "Hybrid",
            mileage: "Unlimited",
            luggage: "3 Large Bags",
            year: 2023,
        },
        images: [
            "/placeholder.svg?height=400&width=600",
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
    }

    return (
        <div className="flex flex-col min-h-screen">
            <header className="px-4 lg:px-6 h-16 flex items-center justify-between border-b">
                <Link className="flex items-center gap-2 font-semibold" href="/">
                    <Car className="h-6 w-6 text-emerald-600" />
                    <span>DriveEasy</span>
                </Link>
                <nav className="hidden md:flex gap-6">
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/">
                        Home
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4 text-emerald-600" href="/cars">
                        Cars
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/locations">
                        Locations
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/about">
                        About
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/contact">
                        Contact
                    </Link>
                </nav>
                <div className="flex items-center gap-4">
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/login">
                        Log In
                    </Link>
                    <Button asChild size="sm">
                        <Link href="/register">Sign Up</Link>
                    </Button>
                </div>
            </header>
            <main className="flex-1">
                <div className="container px-4 py-6 md:px-6 md:py-12">
                    <div className="flex items-center gap-2 mb-4">
                        <Button variant="ghost" size="sm" asChild className="gap-1">
                            <Link href="/cars">
                                <ArrowLeft className="h-4 w-4" />
                                Back to Cars
                            </Link>
                        </Button>
                        <div className="flex items-center">
                            <ChevronRight className="h-4 w-4 text-gray-400" />
                            <span className="ml-1 text-sm text-gray-500">{car.name}</span>
                        </div>
                    </div>

                    <div className="grid gap-6 lg:grid-cols-[2fr_1fr] lg:gap-12">
                        <div>
                            <h1 className="text-3xl font-bold mb-2">{car.name}</h1>
                            <div className="flex flex-wrap items-center gap-x-4 gap-y-2 mb-6">
                                <div className="flex items-center">
                                    <Star className="h-4 w-4 text-yellow-400 fill-yellow-400" />
                                    <span className="ml-1 font-medium">{car.rating}</span>
                                    <span className="ml-1 text-gray-500">({car.reviews} reviews)</span>
                                </div>
                                <div className="flex items-center">
                                    <MapPin className="h-4 w-4 text-gray-500" />
                                    <span className="ml-1">{car.location}</span>
                                </div>
                                <div className="flex items-center">
                                    <Users className="h-4 w-4 text-gray-500" />
                                    <span className="ml-1">{car.specifications.seats} Seats</span>
                                </div>
                                <div className="flex items-center">
                                    <Settings className="h-4 w-4 text-gray-500" />
                                    <span className="ml-1">{car.specifications.transmission}</span>
                                </div>
                                <div className="flex items-center">
                                    <Fuel className="h-4 w-4 text-gray-500" />
                                    <span className="ml-1">{car.specifications.fuel}</span>
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-2 mb-8">
                                <Image
                                    src={car.images[0] || "/placeholder.svg"}
                                    alt={car.name}
                                    width={600}
                                    height={400}
                                    className="w-full h-64 object-cover rounded-lg"
                                />
                                <div className="grid grid-cols-2 gap-2">
                                    {car.images.slice(1, 5).map((image, index) => (
                                        <Image
                                            key={index}
                                            src={image || "/placeholder.svg"}
                                            alt={`${car.name} view ${index + 2}`}
                                            width={300}
                                            height={200}
                                            className="w-full h-[120px] object-cover rounded-lg"
                                        />
                                    ))}
                                </div>
                            </div>

                            <Tabs defaultValue="description" className="mb-8">
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
                                    <p className="text-gray-700">{car.description}</p>
                                </TabsContent>
                                <TabsContent value="features" className="pt-4">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
                                        {car.features.map((feature, index) => (
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
                                            <span className="font-medium">{car.specifications.seats}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Doors</span>
                                            <span className="font-medium">{car.specifications.doors}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Transmission</span>
                                            <span className="font-medium">{car.specifications.transmission}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Fuel Type</span>
                                            <span className="font-medium">{car.specifications.fuel}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Mileage</span>
                                            <span className="font-medium">{car.specifications.mileage}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Luggage</span>
                                            <span className="font-medium">{car.specifications.luggage}</span>
                                        </div>
                                        <div className="flex justify-between py-2 border-b">
                                            <span className="text-gray-500">Year</span>
                                            <span className="font-medium">{car.specifications.year}</span>
                                        </div>
                                    </div>
                                </TabsContent>
                            </Tabs>

                            <div className="mb-8">
                                <h2 className="text-xl font-bold mb-4">Similar Cars</h2>
                                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                                    {car.similar.map((similarCar) => (
                                        <Link key={similarCar.id} href={`/cars/${similarCar.id}`} className="block group">
                                            <div className="border rounded-lg overflow-hidden transition-all group-hover:shadow-md">
                                                <Image
                                                    src={similarCar.image || "/placeholder.svg"}
                                                    alt={similarCar.name}
                                                    width={300}
                                                    height={200}
                                                    className="w-full h-40 object-cover"
                                                />
                                                <div className="p-3">
                                                    <div className="flex justify-between items-start">
                                                        <div>
                                                            <h3 className="font-bold group-hover:text-emerald-600 transition-colors">
                                                                {similarCar.name}
                                                            </h3>
                                                            <p className="text-sm text-gray-500">{similarCar.category}</p>
                                                        </div>
                                                        <div className="text-right">
                                                            <span className="font-bold">${similarCar.price}</span>
                                                            <p className="text-xs text-gray-500">per day</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </Link>
                                    ))}
                                </div>
                            </div>
                        </div>

                        <div className="lg:sticky lg:top-6 h-fit">
                            <div className="border rounded-lg overflow-hidden">
                                <div className="bg-emerald-50 p-4 border-b">
                                    <div className="flex justify-between items-center">
                                        <div>
                                            <span className="text-2xl font-bold">${car.price}</span>
                                            <span className="text-gray-500"> / day</span>
                                        </div>
                                        <div className="flex items-center">
                                            <Star className="h-4 w-4 text-yellow-400 fill-yellow-400" />
                                            <span className="ml-1 font-medium">{car.rating}</span>
                                        </div>
                                    </div>
                                </div>
                                <div className="p-4">
                                    <form className="space-y-4">
                                        <div className="space-y-2">
                                            <label className="text-sm font-medium">Pick-up & Return Date</label>
                                            <DatePickerWithRange className="w-full" />
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
                                                        <SelectItem value="nyc">New York City</SelectItem>
                                                        <SelectItem value="la">Los Angeles</SelectItem>
                                                        <SelectItem value="chicago">Chicago</SelectItem>
                                                        <SelectItem value="miami">Miami</SelectItem>
                                                    </SelectContent>
                                                </Select>
                                            </div>
                                        </div>
                                        <div className="space-y-2">
                                            <label className="text-sm font-medium">Return Location</label>
                                            <div className="flex items-center gap-2 rounded-md border p-2">
                                                <MapPin className="h-4 w-4 text-gray-500" />
                                                <Select>
                                                    <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                                        <SelectValue placeholder="Same as pick-up" />
                                                    </SelectTrigger>
                                                    <SelectContent>
                                                        <SelectItem value="same">Same as pick-up</SelectItem>
                                                        <SelectItem value="nyc">New York City</SelectItem>
                                                        <SelectItem value="la">Los Angeles</SelectItem>
                                                        <SelectItem value="chicago">Chicago</SelectItem>
                                                        <SelectItem value="miami">Miami</SelectItem>
                                                    </SelectContent>
                                                </Select>
                                            </div>
                                        </div>
                                        <div className="space-y-2">
                                            <label className="text-sm font-medium">Extra Options</label>
                                            <div className="space-y-2">
                                                <div className="flex items-center justify-between">
                                                    <div className="flex items-center gap-2">
                                                        <input type="checkbox" id="gps" className="rounded text-emerald-600" />
                                                        <label htmlFor="gps">GPS Navigation</label>
                                                    </div>
                                                    <span>$5/day</span>
                                                </div>
                                                <div className="flex items-center justify-between">
                                                    <div className="flex items-center gap-2">
                                                        <input type="checkbox" id="child-seat" className="rounded text-emerald-600" />
                                                        <label htmlFor="child-seat">Child Seat</label>
                                                    </div>
                                                    <span>$7/day</span>
                                                </div>
                                                <div className="flex items-center justify-between">
                                                    <div className="flex items-center gap-2">
                                                        <input type="checkbox" id="additional-driver" className="rounded text-emerald-600" />
                                                        <label htmlFor="additional-driver">Additional Driver</label>
                                                    </div>
                                                    <span>$10/day</span>
                                                </div>
                                            </div>
                                        </div>
                                        <Separator />
                                        <div className="space-y-2">
                                            <div className="flex justify-between">
                                                <span>Base Rate (3 days)</span>
                                                <span>${car.price * 3}</span>
                                            </div>
                                            <div className="flex justify-between text-gray-500">
                                                <span>Taxes & Fees</span>
                                                <span>${Math.round(car.price * 3 * 0.15)}</span>
                                            </div>
                                            <div className="flex justify-between text-gray-500">
                                                <span>Security Deposit (refundable)</span>
                                                <span>${car.deposit}</span>
                                            </div>
                                            <div className="flex justify-between font-bold text-lg pt-2">
                                                <span>Total</span>
                                                <span>${car.price * 3 + Math.round(car.price * 3 * 0.15)}</span>
                                            </div>
                                        </div>
                                        <Button className="w-full bg-emerald-600 hover:bg-emerald-700">Book Now</Button>
                                        <p className="text-xs text-center text-gray-500">
                                            You won't be charged yet. We'll confirm availability first.
                                        </p>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
            <footer className="flex flex-col gap-2 sm:flex-row py-6 w-full shrink-0 items-center px-4 md:px-6 border-t">
                <p className="text-xs text-gray-500">© 2023 DriveEasy. All rights reserved.</p>
                <nav className="sm:ml-auto flex gap-4 sm:gap-6">
                    <Link className="text-xs hover:underline underline-offset-4" href="#">
                        Terms of Service
                    </Link>
                    <Link className="text-xs hover:underline underline-offset-4" href="#">
                        Privacy
                    </Link>
                    <Link className="text-xs hover:underline underline-offset-4" href="#">
                        Cookies
                    </Link>
                </nav>
            </footer>
        </div>
    )
}
