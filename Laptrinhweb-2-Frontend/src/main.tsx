import { createRoot } from 'react-dom/client';
import './index.css';
import RootLayout from './Root.tsx';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import SignIn from './pages/auth/SignIn/SignIn.tsx';
import SignUp from './pages/auth/SignUp/SignUp.tsx';
import HomePage from './pages/home/HomePage.tsx';
import ForgotPassword from './pages/auth/forgot/ForgotPassword.tsx';
import CompanyDetail from './pages/company/CompanyDetail.tsx';
import CompanyPage from './pages/company/Company.tsx';
import UserDashboard from './pages/dashboard/user/UserDashboard.tsx';
import CompanyManager from './pages/dashboard/admin/CompanyManager.tsx';
import UserManager from './pages/dashboard/admin/UserManager.tsx';

const router = createBrowserRouter([
  {
    path: '/',
    Component: RootLayout,
    children: [
      {
        path: 'auth',
        children: [
          {
            path: 'sign-in',
            Component: SignIn
          },
          {
            path: 'sign-up',
            Component: SignUp
          },
          {
            path: 'forgot',
            Component: ForgotPassword
          }
        ]
      },
      {
        path: 'company',
        Component: CompanyPage,
        children: [
          {
            path: ':id',
            Component: CompanyDetail
          }
        ]
      },
      {
        path: 'home',
        Component: HomePage
      },
      {
        path: 'user-profile',
        Component: UserDashboard
      },
      {
        path: 'admin',
        children: [
          {
            path: 'company-manager',
            Component: CompanyManager,
          },
          {
            path: 'user-manager',
            Component: UserManager
          }
        ]
      }
    ]
  }
]);

createRoot(document.getElementById('root')!).render(<RouterProvider router={router} />);
