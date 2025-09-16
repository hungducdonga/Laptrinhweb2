import React from 'react';
import CompanyInterface from '../../../interface/company/companyResponse';

interface OptionInterface {
  value: string;
  label: string;
}

interface SelectionComponentProps {
  optionList?: (OptionInterface | undefined)[];
  optionCompany?: (CompanyInterface | undefined)[];
  selectValue?: string;
  setSelectValue: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  setField: (field: string, value: string) => void;
  name?: string;
}
const SelectionComponent = ({
  optionCompany,
  optionList,
  selectValue,
  setSelectValue,
  name
}: SelectionComponentProps) => {
  return (
    <div
      className={`w-full border text-[13px] truncate text-[#1c1c1c] dark:text-[#f9f9f9] rounded-sm py-2 px-3 placeholder:text-[#1c1c1c] dark:placeholder:text-[#464646] outline-[#3a3a3a] focus:outline-blue-600 focus:placeholder:text-transparent dark:focus:outline-blue-600`}
    >
      <select className='w-full' name={name} value={selectValue} onChange={setSelectValue}>
        {optionCompany
          ? optionCompany?.map((option) =>
              option ? (
                <option className='w-full' key={option.id} value={option.id}>
                  {option.companyName}
                </option>
              ) : null
            )
          : optionList?.map((option, index) =>
              option ? (
                <option className='w-full' key={index} value={option.value}>
                  {option.label}
                </option>
              ) : null
            )}
      </select>
    </div>
  );
};

export default SelectionComponent;
