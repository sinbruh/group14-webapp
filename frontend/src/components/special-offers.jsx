import Image from "next/image"
import Link from "next/link"
import { Clock } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card"

export function SpecialOffers() {
    const offers = [
        {
            id: 1,
            title: "Weekend Getaway Special",
            description: "Get 15% off on weekend rentals. Perfect for your short trips.",
            image: "/placeholder.svg?height=200&width=400",
            expiryDays: 7,
        },
        {
            id: 2,
            title: "Summer Road Trip Deal",
            description: "Book for 7+ days and get one day free plus unlimited mileage.",
            image: "/placeholder.svg?height=200&width=400",
            expiryDays: 14,
        },
        {
            id: 3,
            title: "Business Travel Package",
            description: "Special rates for business travelers with premium vehicle options.",
            image: "/placeholder.svg?height=200&width=400",
            expiryDays: 30,
        },
    ]

    return (
        <section className="w-full py-12 md:py-24 lg:py-32 bg-emerald-50">
            <div className="container px-4 md:px-6">
                <div className="flex flex-col items-center justify-center space-y-4 text-center">
                    <div className="space-y-2">
                        <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">Special Offers</h2>
                        <p className="max-w-[900px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
                            Take advantage of our limited-time deals and save on your next rental.
                        </p>
                    </div>
                </div>
                <div className="mx-auto grid max-w-5xl items-center gap-6 py-12 md:grid-cols-2 lg:grid-cols-3">
                    {offers.map((offer) => (
                        <Card key={offer.id} className="overflow-hidden">
                            <CardHeader className="p-0">
                                <Image
                                    src={offer.image || "/placeholder.svg"}
                                    alt={offer.title}
                                    width={400}
                                    height={200}
                                    className="w-full object-cover h-48"
                                />
                            </CardHeader>
                            <CardContent className="p-4">
                                <h3 className="text-xl font-bold">{offer.title}</h3>
                                <p className="mt-2 text-gray-500">{offer.description}</p>
                                <div className="mt-4 flex items-center text-sm text-gray-500">
                                    <Clock className="mr-1 h-4 w-4" />
                                    <span>Expires in {offer.expiryDays} days</span>
                                </div>
                            </CardContent>
                            <CardFooter className="p-4 pt-0">
                                <Button asChild className="w-full bg-emerald-600 hover:bg-emerald-700">
                                    <Link href={`/offers/${offer.id}`}>View Offer</Link>
                                </Button>
                            </CardFooter>
                        </Card>
                    ))}
                </div>
            </div>
        </section>
    )
}
