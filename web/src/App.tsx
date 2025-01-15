import { useEffect, useState } from 'react';
import './App.css';
import { LoginOption, MyData } from './types';
import { authMiddlewareApi } from './services/auth-middleware/api';

function App() {
  const [user, setUser] = useState<MyData | null>(null);

  const getLoginOptions = async (): Promise<LoginOption[]> => {
    return await authMiddlewareApi
      .get<LoginOption[]>('/login-options')
      .then((response) => response.data);
  };

  const getMyData = async (): Promise<MyData> => {
    return await authMiddlewareApi
      .get<MyData>('/me', {
        withCredentials: true,
      })
      .then((response) => response.data);
  };

  useEffect(() => {
    getMyData().then((data) => setUser(data));
  }, []);

  const login = async () => {
    const loginOptions = await getLoginOptions();

    if (loginOptions.length > 0) {
      const loginUri = new URL(loginOptions[0].loginUri);

      window.location.href = loginUri.toString();
    }
  };

  const logout = async () => {
    const baseUri = new URL(import.meta.url);
    const logoutUri = new URL(`${baseUri.origin}/logout`);

    window.location.href = logoutUri.toString();
  };

  return (
    <>
      {user ? (
        <div>
          <p>Olá, {user.attributes.name}</p>
          <button onClick={logout}>Logout</button>
        </div>
      ) : (
        <button onClick={login}>Login</button>
      )}
    </>
  );
}

export default App;
