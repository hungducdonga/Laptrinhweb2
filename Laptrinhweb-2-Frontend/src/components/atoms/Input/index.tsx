import { useState } from 'react';
import SelectionComponent from '../Selection';
import { EyeInvisibleOutlined, EyeOutlined } from '@ant-design/icons';
import CompanyInterface from '../../../interface/company/companyResponse';

interface OptionInterface {
  value: string;
  label: string;
}
interface CommonInputProps {
  typeInput: string;
  inputValue?: string;
  error?: string;
  field: string;
  nameInput?: string;
  nameSelect?: string;
  setField: (field: string, value: string) => void;
  onblur: () => void;
  label_title?: string;
  labelTileClassName?: string;
  inputClassName?: string;
  iconPassStyle?: string;
  optionList?: (OptionInterface | undefined)[];
  optionCompany?: (CompanyInterface | undefined)[];
  hidden?: boolean;
  iconPass?: boolean;
  passHidden?: boolean;
  setPassHidden?: (value: boolean) => void;
  placeholder?: string;
}

const CommonInput = ({
  inputValue,
  error,
  onblur,
  label_title,
  iconPassStyle,
  optionList,
  optionCompany,
  typeInput,
  setField,
  field,
  hidden,
  passHidden,
  iconPass,
  setPassHidden,
  placeholder,
  nameInput,
  nameSelect
}: CommonInputProps) => {
  const [selectValue, setSelectValue] = useState<string>('');

  const handlePageSizeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selectValue = e.target.value;
    setSelectValue(selectValue);
    setField(field, selectValue);
    console.log('field: ', field);
    console.log('selectValue: ', selectValue);
  };

  return (
    <div
      className={`relative w-full px-3 py-2 flex flex-col justify-start items-start gap-1 ${
        passHidden ? 'relative justify-end' : ''
      }`}
    >
      <label
        htmlFor=''
        className={
          hidden
            ? 'font-bold text-[14px] text-[#252525] dark:text-[#e6e6e6]'
            : `after:content-['*'] after:ml-0.5 after:text-red-500 font-bold text-[14px] text-[#252525] dark:text-[#e6e6e6]`
        }
      >
        {label_title}
      </label>
      {optionList || optionCompany ? (
        <SelectionComponent
          optionList={optionList}
          optionCompany={optionCompany}
          selectValue={selectValue}
          setSelectValue={handlePageSizeChange}
          setField={setField}
          name={nameSelect}
        />
      ) : (
        <input
          onBlur={onblur}
          className={`w-full border text-[13px] truncate text-[#1c1c1c] dark:text-[#f9f9f9] rounded-sm py-2 px-3 placeholder:text-[#1c1c1c] dark:placeholder:text-[#464646] outline-[#3a3a3a] focus:outline-blue-600 focus:placeholder:text-transparent dark:focus:outline-blue-600`}
          type={passHidden ? 'text' : typeInput}
          value={inputValue}
          name={nameInput}
          onChange={(e) => setField(field, e.target.value)}
          placeholder={placeholder}
          required={true}
        />
      )}
      {iconPass ? (
        passHidden ? (
          <EyeInvisibleOutlined
            onClick={() => setPassHidden!(!passHidden)}
            className={`absolute right-6 bottom-4 cursor-pointer ${iconPassStyle}`}
          />
        ) : (
          <EyeOutlined
            onClick={() => setPassHidden!(!passHidden)}
            className={`absolute right-6 bottom-4 cursor-pointer ${iconPassStyle}`}
          />
        )
      ) : null}
      {error && <span className='mt-1 text-[10px] text-red-500'>{error}</span>}
    </div>
  );
};

export default CommonInput;
