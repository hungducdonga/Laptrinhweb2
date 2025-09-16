import { useEffect, useState } from 'react';
import ModalCommon from '../../../components/atoms/Modal';
import Decentralization from '../../../components/Decentralization';
import UserInterface from '../../../interface/user/userResponse';
import UserService from '../../../api/services/UserService';
import { useNavigate } from 'react-router';
import { handleError } from '../../../utils/helper';

const UserManager = () => {
  const [openModal, setOpenModal] = useState<boolean>(false);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [userAllResponse, setAllUserResponse] = useState<UserInterface[]>();
  const [userAllFiltered, setUserAllFiltered] = useState<UserInterface[]>();
  const navigate = useNavigate();

  console.log('userAllResponse: ', userAllResponse)

  const handleOpenModal = (userId: number) => {
    setOpenModal(!openModal);
    setSelectedUserId(userId);
  };

  useEffect(() => {
    fetchAllUserData();
  }, []);

  const fetchAllUserData = () => {
    if (UserService.isAuthenticated() && localStorage.getItem('token')) {
      const token = localStorage.getItem('token');
      const response = UserService.getAllUsers(token!);
      response.then((data) => {
        setAllUserResponse(data);
        console.log('data: ', data);
        const userFiltered = data?.filter(
          (user: UserInterface) => user.authorities.length === 1 && user.authorities[0].authority === 'ROLE_USER'
        );
        setUserAllFiltered(userFiltered);
      });
    }
  };

  const deleteUserHandler = async (userId: number) => {
    try {
      const token = localStorage.getItem('token');
      if (UserService.isAuthenticated() && UserService.isAdmin() && token) {
        const response = await UserService.deleteUser(token, userId);
        navigate('/admin/user-manager');
        return response;
      }
    } catch (error) {
      const message = handleError(error)
      throw new Error(message);
    }
  };

  return (
    <div className='relative w-full min-h-screen pt-24 flex flex-col items-center justify-start'>
      <h2 className='text-3xl font-bold py-3'>User Manager</h2>
      <div className='w-[80%] h-[70vh] m-auto overflow-y-auto overflow-hidden'>
        <table cellPadding={2} cellSpacing={2} className='w-full py-2 px-2 table-fixed'>
          <thead className='md:w-full py-2 text-slate-100'>
            <th className='sticky top-0 bg-blue-500 w-1/6 truncate py-2 border border-blue-500'>#Id</th>
            <th className='sticky top-0 bg-blue-500 w-1/5 truncate py-2 border border-blue-500'>First Name</th>
            <th className='sticky top-0 bg-blue-500 w-1/5 truncate py-2 border border-blue-500'>Last Name</th>
            <th className='sticky top-0 bg-blue-500 w-1/3 truncate py-2 border border-blue-500'>Email</th>
            <th className='sticky top-0 bg-blue-500 w-1/5 truncate py-2 border border-blue-500'>Role</th>
            <th className='sticky top-0 bg-blue-500 w-1/4 truncate py-2 border border-blue-500'>Action</th>
          </thead>
          <tbody className='md:w-full bg-slate-100 text-slate-800'>
            {userAllFiltered?.map((item) => (
              <tr key={item.id} className='w-full text-center truncate'>
                <td className='py-2 border border-blue-500 truncate px-2'>{item.id}</td>
                <td className='py-2 border border-blue-500 truncate px-2'>{item.firstName}</td>
                <td className='py-2 border border-blue-500 truncate px-2'>{item.lastName}</td>
                <td className='py-2 border border-blue-500 truncate px-2'>{item.email}</td>
                <td className='py-2 border border-blue-500 truncate px-2'>
                  <ul>
                    {item.authorities.map((role) => (
                      <li key={role.roleId}>{role.authority}</li>
                    ))}
                  </ul>
                </td>
                <td className='py-2 border border-blue-500 truncate px-2 flex items-center justify-center gap-3'>
                  <button
                    onClick={() => handleOpenModal(item.id)}
                    className='bg-green-500 text-xs text-slate-100 font-bold px-2 py-1 rounded-md'
                  >
                    role
                  </button>
                  <button
                    onClick={() => deleteUserHandler(item.id)}
                    className='bg-red-500 text-xs text-slate-100 font-bold px-2 py-1 rounded-md'
                  >
                    Del
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <ModalCommon isOpen={openModal} setIsOpen={setOpenModal}>
        <Decentralization selectedUserId={selectedUserId} />
      </ModalCommon>
    </div>
  );
};

export default UserManager;
