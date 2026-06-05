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
    const [discountCode, setDiscountCode] = useState('');

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
            // Try /cart/:userId then fallback to /cart?userId=
            let res = await api.get(`/cart/${userId}`);
            let data = res.data;
            if (!Array.isArray(data)) {
                // try common shapes
                if (data && Array.isArray(data.items)) data = data.items;
                else if (data && Array.isArray(data.cart)) data = data.cart;
                else if (data && Array.isArray(data.data)) data = data.data;
                else {
                    const arr = Object.values(data).find(v => Array.isArray(v));
                    if (arr) data = arr;
                    else data = [];
                }
            }
            setItems(data);
            // update header count
            window.dispatchEvent(new CustomEvent('cartUpdated', { detail: data.length }));
        } catch (err: any) {
            // fallback try query param
            try {
                const userId = localStorage.getItem('userId');
                const res2 = await api.get(`/cart/${userId}`);
                let data2 = res2.data;
                if (!Array.isArray(data2)) {
                    if (data2 && Array.isArray(data2.items)) data2 = data2.items;
                    else data2 = [];
                }
                setItems(data2);
                window.dispatchEvent(new CustomEvent('cartUpdated', { detail: data2.length }));
            } catch (err2: any) {
                setError(err?.response?.data?.message || err?.message || 'Failed to load cart');
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchCart(); }, []);

    const handleRemove = async (productId?: string | number) => {
        if (!productId) return;
        try {
            // Get cartId from the stored user object
            const userStr = localStorage.getItem('userId');
            const cartId = userStr;

            if (!cartId) {
                setError('Cart ID not found');
                return;
            }

            // DELETE /cart/remove with RemoveProductFromCartRequest: { cartId, productId }
            // Use request body with axios DELETE
            await api.delete('/cart/remove', {
                data: {
                    cartId: cartId,
                    productId: productId
                }
            });

            await fetchCart();
        } catch (err: any) {
            setError(err?.response?.data?.message || err.message || 'Failed to remove item');
        }
    };

    const handleCheckout = async () => {
        if (!discountCode.trim()) {
            setError('Please enter a discount code');
            return;
        }

        try {
            // POST /checkout
            const res = await api.post('/checkout', {
                userId: localStorage.getItem('userId'),
                discountCode: discountCode,
            });

            if (res.data.success) {
                alert(`Order created successfully! Order ID: ${res.data.orderId}`);
                setDiscountCode('');
                fetchCart();
            } else {
                setError(res.data.message || 'Failed to create order');
            }
        } catch (err: any) {
            setError(err?.response?.data?.message || err.message || 'Failed to create order');
        }
    };

    if (loading) return (
        <Container className="py-5">
            <div className="d-flex justify-content-center"><Spinner animation="border"/></div>
        </Container>
    );

    if (error) return (
        <Container className="py-5">
            <Alert variant="danger">{error}</Alert>
        </Container>
    );

    return (
        <Container className="py-5">
            <h1 className="mb-4">Your Cart</h1>
            {items.length === 0 ? (
                <Alert variant="info">Your cart is empty.</Alert>
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

                    <div className="mt-4">
                        <h3>Checkout</h3>
                        <Form.Group controlId="discountCode">
                            <Form.Label>Discount Code (optional)</Form.Label>
                            <Form.Control type="text" value={discountCode} onChange={(e) => setDiscountCode(e.target.value)} />
                        </Form.Group>

                        <Button variant="primary" onClick={handleCheckout}>Checkout</Button>
                    </div>
                </>
            )}
        </Container>
    );
};

export default CartList;