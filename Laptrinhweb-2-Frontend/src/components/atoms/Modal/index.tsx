import { CloseOutlined } from '@ant-design/icons';
import React from 'react';

interface ModalCommonProps {
  children: React.ReactNode;
  isOpen: boolean;
  setIsOpen: (value: boolean) => void;
}

const ModalCommon = ({ children, isOpen, setIsOpen }: ModalCommonProps) => {
  const onClose = () => {
    setIsOpen(!isOpen);
  };
  return (
    <div
      className={`${isOpen ? 'absolute top-0 w-full h-full flex justify-center items-center bg-[#1f1f1f4e]' : 'hidden'}`}
    >
      <button
        onClick={onClose}
        className='absolute right-5 top-28 px-2 py-1 rounded-md text-red-500 text-xl font-bold hover:text-slate-50 hover:bg-red-600'
      >
        <CloseOutlined />
      </button>
      {children}
    </div>
  );
};

export default ModalCommon;
