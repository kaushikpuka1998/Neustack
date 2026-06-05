// src/apiTypes.ts
export interface ApiResponse {
    data: any;
    message?: string;
    success: boolean;
}

// src/interfaces/apiTypes.ts
export interface Product {
    id: string;
    name: string;
    price: number;
    stock: number;
    img_url: string;
    description: string;
}