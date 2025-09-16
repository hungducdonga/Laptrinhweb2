import { Link } from 'react-router';
import './index.css';

const items = [
  {
    id: 1,
    title: 'Home',
    url_path: '/'
  },
  {
    id: 2,
    title: 'About',
    url_path: '/'
  },
  {
    id: 3,
    title: 'Contact',
    url_path: '/'
  }
];

const menuItem = items.map((item) => (
  <li key={item.id} className='li_menu z-0'>
    <Link to={`${item.url_path}`}>{item.title}</Link>
  </li>
));

const FooterCommon = () => {
  return (
    <footer className='w-full flex flex-col justify-start items-center py-3 px-3 border bg-slate-200 shadow-md shadow-slate-500 z-0'>
      <section className='w-full h-full flex'>
        <aside className='w-1/3 px-2 flex items-center justify-center'>
          <h2 className='font-bold text-2xl text-slate-900'>Welcome</h2>
        </aside>
        <ul className='w-1/2 flex flex-col justify-start items-start gap-2 z-0'>{menuItem}</ul>
      </section>
      <section className='w-full flex items-center justify-center py-3'>
        <p className='text-xs font-bold text-center'>Copy right | 🎯💝💘</p>
      </section>
    </footer>
  );
};

export default FooterCommon;
