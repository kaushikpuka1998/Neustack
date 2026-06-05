import { type FC, useEffect, useState } from 'react';
import { Container, Row, Col, Card, Spinner, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { api } from '../../config';

type Product = {
  imgUrl: string;
  id: number | string;
  name?: string;
  price?: number;
  stock?: number;
  image?: string;
  description?: string;
};

const Products: FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleProductClick = (productId: number | string) => {
    navigate(`/products/${productId}`);
  };

  useEffect(() => {
    let mounted = true;

    const fetchProducts = async () => {
      setLoading(true);
      setError(null);
      try {

        // Try /product endpoint first (user's API), fallback to /products if needed
        const res = await api.get('/products');
        if (!mounted) return;
        const data = res.data;
        // console.debug('API Response from /product:', JSON.stringify(data, null, 2));
        let productArray: Product[] = [];
        if (data && Array.isArray(data.data)) {
          productArray = data.data;
        }
        setProducts(productArray);
      } catch (err: any) {
        setError(err?.response?.data?.message || err.message || 'Failed to load products');
      } finally {
        if (mounted) setLoading(false);
      }
    };

    fetchProducts();

    return () => { mounted = false };
  }, []);

  return (
    <Container className="py-5">
      <h1 className="mb-4">Products</h1>

      {loading && (
        <div className="d-flex justify-content-center py-5">
          <Spinner animation="border" role="status" />
        </div>
      )}

      {error && (
        <Alert variant="danger">{error}</Alert>
      )}

      {!loading && !error && products.length === 0 && (
        <Alert variant="info">No products found.</Alert>
      )}

      <Row xs={1} md={3} className="g-4">
        {products.map((p, idx) => (
          <Col key={(p.id ?? idx) as unknown as string}>
            <Card
              onClick={() => handleProductClick(p.id)}
              style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
              className="h-100"
              onMouseEnter={(e) => (e.currentTarget.style.transform = 'translateY(-5px)')}
              onMouseLeave={(e) => (e.currentTarget.style.transform = 'translateY(0)')}
            >
              <Card.Img
                variant="top"
                src={p.imgUrl}
                alt={p.name}
              />
              <Card.Body>
                <Card.Title>{p.name}</Card.Title>
                <Card.Text>{p.description ?? 'No description available.'}</Card.Text>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default Products;
