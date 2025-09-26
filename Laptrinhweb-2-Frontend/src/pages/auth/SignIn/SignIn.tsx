// Import các thành phần và hooks cần thiết
import CommonInput from '../../../components/atoms/Input';          // Input tùy biến của dự án
import { handleBlurChecking, handleError } from '../../../utils/helper'; // Hàm tiện ích: validate & xử lý lỗi
import useCombinedState from '../../../hooks/useCombinedState';    // Hook quản lý nhiều state trong 1 object
import { BaseSyntheticEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router';
import { CheckCircleFilled, FacebookOutlined, GooglePlusOutlined } from '@ant-design/icons';
import UserService from '../../../api/services/UserService';       // Service gọi API đăng nhập
import Notification from '../../../components/Notification';       // Component hiển thị thông báo

const SignIn = () => {
  // State hiển thị thông báo thành công
  const [notification, setNotification] = useState<boolean>(false);
  // State ẩn/hiện mật khẩu (icon mắt)
  const [passHidden, setPassHidden] = useState<boolean>(false);
  // Lưu thông tin lỗi khi login thất bại
  const [error, setError] = useState<string>('');
  // Hook điều hướng
  const navigate = useNavigate();

  // State form gộp: email, password và lỗi tương ứng
  const [state, setField] = useCombinedState({
    email: '',
    password: '',
    emailError: '',
    passwordError: ''
  });

  // Hàm xử lý submit form đăng nhập
  const loginSubmit = async (e: BaseSyntheticEvent<Event, EventTarget & HTMLFormElement>) => {
    e.preventDefault(); // Ngăn reload trang
    // Lấy giá trị các input từ form
    const formValues = Object.fromEntries(new FormData(e.target));
    console.log('Form values: ', formValues.email);

    try {
      // Gọi API login: trả về token + role + thông tin user
      const response = await UserService.login(
        formValues.email.toString(),
        formValues.password.toString()
      );

      if (response.data.token) {
        // Lưu token, role, email vào localStorage để dùng cho các request khác
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('role', response.data.role);
        localStorage.setItem('email', response.data.userLoginDto.email);

        // Hiển thị thông báo thành công
        setNotification(true);
        setInterval(() => {
          setNotification(false);
        }, 5000);

        // Chuyển về trang chủ
        navigate('/');
      } else {
        // Nếu không có token (login fail) => hiển thị lỗi
        setError(response.error);
        setNotification(true);
        setInterval(() => {
          setNotification(false);
        }, 10000);
        navigate('/auth/sign-in');
      }
    } catch (err) {
      // Xử lý ngoại lệ (network/server error)
      const message = handleError(err);
      setError(message!);
      console.log(error);
      setTimeout(() => {
        setError('');
      }, 5000);
      navigate('/auth/sign-in');
    }
  };

  return (
    <div className='relative w-full h-full pt-20 px-2 pb-3 flex flex-col items-center justify-center'>
      {/* Khung form đăng nhập */}
      <div className='md:w-1/3 sm:w-1/2 w-3/4 border rounded-md shadow-md shadow-slate-500 py-3 px-2 flex flex-col items-center justify-around'>
        <h2 className='text-2xl font-bold'>Sign In</h2>

        {/* Form đăng nhập */}
        <form onSubmit={loginSubmit} className='w-full'>
          {/* Ô nhập Email */}
          <CommonInput
            onblur={() => handleBlurChecking('emailError', state.email, setField)} // validate khi blur
            inputValue={state.email}
            typeInput='email'
            setField={setField}
            field='email'
            error={state.emailError}
            hidden={false}
            nameInput='email'
            label_title='Email'
            placeholder='Please, enter email'
            labelTileClassName='text-white'
            inputClassName='border-none bg-[#ffffff1d] text-slate-200 placeholder:text-slate-200 dark:placeholder:text-slate-200 outline-slate-800 focus:outline-green-500 focus:bg-transparent'
          />

          {/* Ô nhập Password */}
          <CommonInput
            onblur={() => handleBlurChecking('passwordError', state.password, setField)}
            inputValue={state.password}
            typeInput='password'
            setField={setField}
            field='password'
            error={state.passwordError}
            hidden={false}
            iconPass={true}           // Hiển thị icon mắt
            passHidden={passHidden}   // Trạng thái ẩn/hiện
            setPassHidden={setPassHidden}
            nameInput='password'
            label_title='Password'
            placeholder='Please, enter password'
            labelTileClassName='text-white'
            inputClassName='border-none bg-[#ffffff1d] text-slate-200 placeholder:text-slate-200 dark:placeholder:text-slate-200 outline-slate-800 focus:outline-green-500 focus:bg-transparent'
          />

          {/* Link quên mật khẩu */}
          <span className='text-xs text-slate-800 py-2 px-4 flex justify-end cursor-pointer'>
            <Link className='text-right' to='/auth/forgot'>
              You have forgot password, right?
            </Link>
          </span>

          {/* Nút submit */}
          <button
            className='w-full py-2 my-2 bg-blue-700 border border-transparent text-slate-50 text-xl font-bold rounded-md shadow-md shadow-slate-600 hover:border-blue-700 hover:bg-blue-600 active:shadow-slate-800'
            type='submit'
          >
            Sign in
          </button>
        </form>

        {/* Đăng nhập mạng xã hội (icon minh họa) */}
        <span className='w-full text-2xl py-3 flex justify-center gap-3'>
          <Link to='/'>
            <FacebookOutlined className='text-blue-700' />
          </Link>
          <Link to='/'>
            <GooglePlusOutlined className='text-red-700' />
          </Link>
        </span>

        {/* Link chuyển sang Sign up */}
        <span className='text-xs text-slate-800 py-2 px-4 flex justify-end cursor-pointer'>
          You have not been an account, right?
          <Link className='text-blue-700 px-1 font-bold' to='/auth/sign-up'>
            Sign up
          </Link>
        </span>
      </div>

      {/* Hiển thị thông báo thành công */}
      {notification && (
        <Notification
          icon={<CheckCircleFilled className='text-4xl text-green-600' />}
          message='Sign in successfully!'
          description='Completed!!!'
          notification={notification}
        />
      )}

      {/* 
        Đoạn code thông báo lỗi (nếu muốn bật)
        <Notification
          icon={<CloseCircleFilled className='text-4xl text-red-600' />}
          message='Sign in fail!'
          description='Not completed!!!'
          notification={notification}
        />
      */}
    </div>
  );
};

export default SignIn;
