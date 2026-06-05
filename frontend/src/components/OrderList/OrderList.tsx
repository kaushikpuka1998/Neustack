// src/components/OrderList.tsx
import {type FC, useEffect, useState} from 'react';
import { Container, Table, Spinner, Alert } from 'react-bootstrap';
import {api} from "../../config.ts";
import {useNavigate} from "react-router-dom";

type Order = {
  id: string;
  user_id: string;
  totalAmount: number;
  discountCode?: string;
  finalAmount: number;
  createdDate: String;
  items: OrderItem[];
};

type OrderItem = {
  id: string;
  name:String
  imgUrl: string;
  productId: string;
  quantity: number;
  price: number;
};

const OrderList: FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [userId] = useState<string | null>(localStorage.getItem('userId'));
  const navigate = useNavigate();

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await api.get((`/users/${userId}/orders`));
        if (res.data.data) {
          setOrders(res.data.data);
        }
      } catch (err: any) {
        setError(err?.response?.data?.message || err.message || 'Failed to load orders');
      } finally {
        setLoading(false)      }
    };

    fetchOrders();
  }, []);

  const handleProductClick = (productId: string) => {
    navigate(`/products/${productId}`);
  };

  const formatDate = (dateString: string): string => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
  };

  return (
    <Container className="py-5">
      {loading && (
        <div className="d-flex justify-content-center py-5">
          <Spinner animation="border" role="status" />
        </div>
      )}

      {error && (
        <Alert variant="danger">{error}</Alert>
      )}

      {!loading && !error && orders.length === 0 && (
        <Alert variant="info">No orders found.</Alert>
      )}

      {orders.length > 0 && (
          <Table responsive>
            <thead>
            <tr>
              <th>Order ID</th>
              <th>Total Amount</th>
              <th>Discount Code</th>
              <th>Final Amount</th>
              <th>Created At</th>
              <th>Items</th>
            </tr>
            </thead>
            <tbody>
            {orders.map((order) => (
                <tr key={order.id}>
                  <td>{order.id}</td>
                  <td>${(order.totalAmount ?? 0).toFixed(2)}</td>
                  <td>{order.discountCode ?? 'No discount code'}</td>
                  <td>${(order.finalAmount ?? 0).toFixed(2)}</td>
                  <td>{formatDate(order.createdDate.trim())}</td>
                  <td>
                    {order.items.length > 0 ? (
                        order.items.map((item) => (
                            <div key={item.productId} onClick={() => handleProductClick(item.productId)}>
                              <img src={item.imgUrl} style={{ width: 80, height: 80, objectFit: 'cover' , cursor: 'pointer' }} />
                              <strong>Product: </strong>{item.name} &nbsp;
                              <strong>Quantity: </strong>{item.quantity} &nbsp;
                              <strong>Price: </strong>${(item.price ?? 0).toFixed(2)}
                              <br />
                            </div>
                        ))
                    ) : (
                        <Alert variant="info">No items in this order.</Alert>
                    )}
                  </td>
                </tr>
            ))}
            </tbody>
          </Table>
      )}
    </Container>
  );
};

export default OrderList;
