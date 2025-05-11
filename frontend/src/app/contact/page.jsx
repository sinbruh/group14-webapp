import Image from "next/image";

export default function Contact() {
    return (
        <div className="container mx-auto py-12 px-4">
            <h1 className="text-3xl font-bold mb-6">Contact Us</h1>
            <div className="grid gap-6 md:grid-cols-2">
                <div>
                    <form className="space-y-4">
                        <input
                            type="text"
                            placeholder="Your Name"
                            className="w-full border rounded-md p-2"
                        />
                        <input
                            type="email"
                            placeholder="Your Email"
                            className="w-full border rounded-md p-2"
                        />
                        <textarea
                            placeholder="Your Message"
                            className="w-full border rounded-md p-2"
                            rows="5"
                        ></textarea>
                        <button
                            type="submit"
                            className="bg-emerald-600 text-white px-4 py-2 rounded-md"
                        >
                            Send Message
                        </button>
                    </form>
                </div>
                <div>
                    <Image
                        src="/placeholder.svg?height=400&width=600"
                        width={600}
                        height={400}
                        alt="Contact Us"
                        className="rounded-lg"
                    />
                </div>
            </div>
        </div>
    );
}