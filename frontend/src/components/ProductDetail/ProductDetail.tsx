import { type FC, useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Card, Spinner, Alert, Button, Row, Col } from 'react-bootstrap';
import { api } from '../../config';

type Product = {
  id: number | string;
  name?: string;
  description?: string;
  price?: number;
  stock?: number;
  imgUrl?: string;
};

const ProductDetail: FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let mounted = true;

    const fetchProductDetail = async () => {
      if (!id) {
        setError('Product ID is missing');
        setLoading(false);
        return;
      }

      setLoading(true);
      setError(null);

      try {
        // Try to fetch product detail by ID
        // Adjust the endpoint based on your backend API (e.g., /product/:id or /product/${id})
        const res = await api.get(`/products/${id}`);
        if (!mounted) return;

        const data = res.data;

        // Extract product from various possible response formats
        let productData: Product | null = null;

        if (data && typeof data === 'object') {
          if (data.product) {
            productData = data.product;
          } else if (data.data) {
            productData = data.data;
          } else if (data.id) {
            productData = data;
          } else {
            // Try to find an object with an id property
            const objectWithId = Object.values(data).find(
              (v) => v && typeof v === 'object' && 'id' in v
            );
            if (objectWithId) {
              productData = objectWithId as Product;
            }
          }
        }

        if (!productData) {
          setError('Product not found');
        } else {
          setProduct(productData);
        }
      } catch (err: any) {
        setError(err?.response?.data?.message || err.message || 'Failed to load product details');
      } finally {
        if (mounted) setLoading(false);
      }
    };

    fetchProductDetail();

    return () => {
      mounted = false;
    };
  }, [id]);

  if (loading) {
    return (
      <Container className="py-5">
        <div className="d-flex justify-content-center">
          <Spinner animation="border" role="status" />
        </div>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">{error}</Alert>
        <Button variant="primary" onClick={() => navigate('/products')}>
          Back to Products
        </Button>
      </Container>
    );
  }

  if (!product) {
    return (
      <Container className="py-5">
        <Alert variant="warning">Product not found</Alert>
        <Button variant="primary" onClick={() => navigate('/products')}>
          Back to Products
        </Button>
      </Container>
    );
  }

  return (
    <Container className="py-5">
      <Button variant="secondary" className="mb-4" onClick={() => navigate('/products')}>
        ← Back to Products
      </Button>

      <Row>
        <Col md={6} className="mb-4">
          <Card>
            <Card.Img
              variant="top"
              src={product.imgUrl}
            />
          </Card>
        </Col>

        <Col md={6}>
          <h1>{product.name || 'Product'}</h1>

          {product.price && (
            <h2 className="text-primary mb-3">${product.price}</h2>
          )}

          {product.stock !== undefined && (
            <p>
              <strong>Stock:</strong> {product.stock > 0 ? `${product.stock} available` : 'Out of stock'}
            </p>
          )}

          {product.description && (
            <div>
              <h5>Description</h5>
              <p>{product.description}</p>
            </div>
          )}

          <div className="mt-4">
            <Button variant="primary" size="lg" className="me-2" onClick={async () => {
              const userId = localStorage.getItem('userId');
              if (!userId) {
                alert('Please sign in first');
                return;
              }
              try {
                // POST to /cart/add with AddCartItemRequest: { userId, productId, quantity }
                await api.post('/cart/add', {
                  userId: userId,
                  productId: String(product.id),
                  quantity: 1
                });

                // Fetch cart to get updated count
                let count = 0;
                try {
                  const res = await api.get(`/cart/${userId}`);
                  const d = res.data;
                  if (Array.isArray(d)) count = d.length;
                  else if (d && Array.isArray(d.items)) count = d.items.length;
                  else if (d && Array.isArray(d.cart)) count = d.cart.length;
                } catch (e) {
                  // ignore
                }

                window.dispatchEvent(new CustomEvent('cartUpdated', { detail: count }));
                navigate("/cart");
              } catch (err: any) {
                alert(err?.response?.data?.message || err?.message || 'Failed to add to cart');
              }
            }}>
              Add to Cart
            </Button>
          </div>

          {/* Display all other product properties */}
          {Object.entries(product).map(([key, value]) => {
            if (['id', 'name', 'price', 'stock', 'imgUrl', 'description'].includes(key)) {
              return null;
            }
            return (
              <div key={key} className="mt-2">
                <strong>{key}:</strong> {String(value)}
              </div>
            );
          })}
        </Col>
      </Row>
    </Container>
  );
};

export default ProductDetail;

