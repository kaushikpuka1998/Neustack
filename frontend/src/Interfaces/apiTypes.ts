// Central API types used by the frontend
export interface ApiResponse<T = any> {
    data: T;
    message?: string;
    success: boolean;
}

// Product returned by backend
export interface Product {
    id: string;
    name?: string;
    price?: number;
    stock?: number;
    image?: string;
    description?: string;
    [key: string]: any;
}

// Cart item used in user's cart
export interface CartItem {
    id?: string;
    productId?: string;
    name?: string;
    quantity?: number;
    price?: number;
    image?: string;
    [key: string]: any;
}

// Cart containing items
export interface Cart {
    id?: string;
    items?: CartItem[];
    total?: number;
    [key: string]: any;
}

// Order simplified type
export interface Order {
    id?: string;
    items?: CartItem[];
    total?: number;
    createdAt?: string;
    status?: string;
    [key: string]: any;
}

// User returned by backend
export interface User {
    id: string;
    name?: string;
    email?: string;
    // password will usually not be returned by backend; kept optional when present
    password?: string;
    cart?: Cart;
    orders?: Order[];
    [key: string]: any;
}