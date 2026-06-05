import { type FC, useEffect, useState } from 'react';
import { Container, Table, Spinner, Alert, Button, Form } from 'react-bootstrap';
import { api } from '../../config';

type CartItem = {
    id?: number | string;
    productId?: number | string;
    name?: string;
    quantity?: number;
    price?: number;
    imgUrl?: string;
    [key: string]: any;
};

const CartList: FC = () => {
    const [items, setItems] = useState<CartItem[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [discountCodeStatus, setDiscountCodeStatus] = useState(false);
    const [discountPercentage, setDiscountPercentage] = useState(0.0);
    const [discountCode, setDiscountCode] = useState('');
    const [totalAmount, setTotalAmount] = useState(0); // Total amount before discount

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {
        const userId = localStorage.getItem('userId');

        if (!userId) {
            setError('Not signed in');
            setLoading(false);
            return;
        }

        setLoading(true);
        setError(null);
        try {
            let res = await api.get(`/cart/${userId}`);
            let data = res.data;

            if (data && Array.isArray(data.items)) data = data.items;
            else if (data && Array.isArray(data.cart)) data = data.cart;
            else if (data && Array.isArray(data.data)) data = data.data;
            else {
                const arr = Object.values(data).find(v => Array.isArray(v));
                if (arr) data = arr;
                else data = [];
            }

            setItems(data);
            setTotalAmount(calculateTotalPrice());
            // update header count
            window.dispatchEvent(new CustomEvent('cartUpdated', { detail: data.length }));
        } catch (err: any) {
            try {
                const userId = localStorage.getItem('userId');
                const res2 = await api.get(`/cart/${userId}`);
                let data2 = res2.data;

                setItems(data2);
                window.dispatchEvent(new CustomEvent('cartUpdated', { detail: data2.length }));
            } catch (err2: any) {
                setError(err?.response?.data?.message || err?.message || 'Failed to load cart');
            }
        } finally {
            setLoading(false);
        }
    };

    const handleRemove = async (productId?: string | number) => {
        if (!productId) return;

        try {
            const userStr = localStorage.getItem('userId');
            const userId = userStr;

            if (!userId) {
                setError('Cart ID not found');
                return;
            }

            await api.delete('/cart/remove', {
                data: {
                    cartId: userId,
                    productId: productId
                }
            });

            fetchCart();
        } catch (err: any) {
            setError(err?.response?.data?.message || err.message || 'Failed to remove item');
        }
    };

    const handleCheckout = async () => {
        try {
            const res = await api.post('/checkout', {
                userId: localStorage.getItem('userId'),
                discountCode: discountCode,
            });

            if (res.data.success) {
                alert(`Order created successfully! Order ID: ${res.data.data.id}`);
                setDiscountCode('');
                fetchCart();
            } else {
                setError(res.data.message || 'Failed to create order');
            }
        } catch (err: any) {
            setError(err?.response?.data?.message || err.message || 'Failed to create order');
        }
    };

    const validateDiscountCode = async () => {
        if (!discountCode.trim()) {
            setError('Please enter a discount code');
            return;
        }

        try {
            const res = await api.post('/admin/discount-code/',  {
                userId: localStorage.getItem('userId'),
                code: discountCode,
            });

            if (res.data.success) {
                // Calculate the final amount
                setTotalAmount(calculateTotalPrice());
                setDiscountCodeStatus(true);
                setDiscountCode('');
                setDiscountPercentage(res.data.data.xPercentage);
                const orders = await api.get(`/users/${localStorage.getItem('userId')}/orders`);
                const everyNthOrder = res.data.data.everyNthOrder;
                if (orders.data.data.length % everyNthOrder === 0) {
                    setDiscountCode(discountCode);
                    alert(`${discountCode} verified and applied`);
                } else {
                    setError(`Discount code not applicable yet. Next applicable after ${everyNthOrder} orders`);
                }


            } else {
                setDiscountCode('');
                setError(res.data.message || 'Failed to create order');
            }
        } catch (err: any) {
            setDiscountCode('');
            setError(err?.response?.data?.message || err.message || 'Failed to create order');
        }
    };

    const calculateTotalPrice = () => {
        if (!items.length) return 0;

        let totalAmount = items.reduce((acc, item) => acc + (item.price), 0);
        setTotalAmount(totalAmount);
        return totalAmount;
    };

    useEffect(() => {
        // Clear error after 10 seconds
        const timer = setTimeout(() => {
            setError(null);
        }, 5000);

        return () => {
            clearTimeout(timer);
        };
    }, [error]);

    if (loading) return (
        <Container className="py-5">
            <div className="d-flex justify-content-center"><Spinner animation="border"/></div>
        </Container>
    );

    const finalPriceDisplay = (
        <div className="mt-4 mb-3">
            <h3>Checkout</h3>
            <p>Total Amount: ${totalAmount.toFixed(2)}</p>
            <p>Discount Code Applied: {discountCode ? `Yes, with a discount of ${((discountPercentage))}%` : 'No'}</p>
            <p>Final Amount After Discount: ${(totalAmount * (1 - (discountPercentage / 100))).toFixed(2)}</p>
        </div>
    );

    return (
        <Container className="py-5">
            <h1 className="mb-4">Your Cart</h1>
            {error && (
                <Alert variant="danger" className="mt-4 mb-3">
                    {error}
                </Alert>
            )}

            {items.length === 0 ? (
                <Alert variant="info">Your cart is empty</Alert>
            ) : (
                <>
                    <Table responsive>
                        <thead>
                        <tr>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th />
                        </tr>
                        </thead>
                        <tbody>
                        {items.map((it) => (
                            <tr key={it.id ?? it.productId}>
                                <td>
                                    <div style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
                                        <img src={it.imgUrl} alt={it.name} style={{ width: 80, height: 80, objectFit: 'cover' }} />
                                        <div>
                                            <div><strong>{it.name ?? `Product ${it.name}`}</strong></div>
                                            <div className="text-muted">{it.description}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>{it.quantity ?? 1}</td>
                                <td>${(it.price ?? 0).toFixed(2)}</td>
                                <td><Button variant="danger" size="sm" onClick={() => handleRemove(it.productId)}>Remove</Button></td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>

                    {discountCodeStatus ? (
                        finalPriceDisplay
                    ) : null}

                    <Form.Group controlId="discountCode">
                        <Form.Control
                            placeholder='Discount Code (optional)'
                            type="text"
                            value={discountCode}
                            onChange={(e) => {
                                setDiscountCode(e.target.value);
                            }} />
                    </Form.Group>

                    <div className="button-container">
                        <Button variant="primary" onClick={validateDiscountCode}>Validate</Button>
                        <Button variant="success" onClick={handleCheckout}>Checkout</Button>
                    </div>
                </>
            )}
        </Container>
    );
};

export default CartList;