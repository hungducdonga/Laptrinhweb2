import { BaseSyntheticEvent } from 'react';
import useCombinedState from '../../hooks/useCombinedState';
import CommonInput from '../atoms/Input';
import { handleBlurChecking, handleError } from '../../utils/helper';
import UserService from '../../api/services/UserService';

interface DecentralizationProps {
  selectedUserId: number | null;
}

const Decentralization = ({ selectedUserId }: DecentralizationProps) => {
  const [state, setField] = useCombinedState({
    role: '',
    roleError: ''
  });

  const submitHandler = async (e: BaseSyntheticEvent<Event, EventTarget & HTMLFormElement>) => {
    e.preventDefault();
    const formValues = Object.fromEntries(new FormData(e.target));
    console.log('Form values: ', formValues);
    try {
      const token = localStorage.getItem('token');
      const decentralizationUser = {
        authorities: [formValues.role.toString()]
      };
      if (UserService.isAuthenticated() && UserService.isAdmin() && token) {
        const response = await UserService.decentralization(token, decentralizationUser, selectedUserId!);
        return response;
      }
    } catch (error) {
      const message = handleError(error)
      throw new Error(message);
    }
  };
  return (
    <div className='w-1/3 flex flex-col justify-start items-center bg-slate-100 rounded-md shadow-md shadow-slate-600 py-2 px-3'>
      <div className='w-full py-3'>
        <h2 className='text-2xl text-center font-bold text-slate-800'>Decentralization {selectedUserId}</h2>
      </div>
      <form className='w-full' onSubmit={submitHandler}>
        <CommonInput
          onblur={() => handleBlurChecking('roleError', state.role, setField)}
          inputValue={state.role}
          typeInput='text'
          setField={setField}
          field='role'
          error={state.roleError}
          hidden={false}
          nameSelect='role'
          nameInput={'role'}
          label_title='Role'
          placeholder='Please, enter role'
        />
        <button
          className='w-full py-2 my-2 bg-blue-700 border border-transparent text-slate-50 text-xl font-bold rounded-md shadow-md shadow-slate-600 hover:border-blue-700 hover:bg-blue-600 active:shadow-slate-800'
          type='submit'
        >
          Save
        </button>
      </form>
    </div>
  );
};

export default Decentralization;
