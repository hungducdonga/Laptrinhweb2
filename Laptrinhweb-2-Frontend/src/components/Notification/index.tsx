import { Flex, Progress } from 'antd';
import React, { useEffect, useState } from 'react';

interface NotificationProps {
  notification: boolean;
  icon: React.ReactNode;
  message: string;
  description: string;
}

const Notification = ({ notification, icon, message, description }: NotificationProps) => {
  const [percent, setPercent] = useState<number>(0);

  useEffect(() => {
    const totalDuration = 10000;
    const intervalDuration = 100;
    const totalSteps = totalDuration / intervalDuration;
    let currentStep = 0;
    const interval = setTimeout(() => {
      currentStep++;
      setPercent((prev) => {
        if (currentStep > totalSteps) {
          clearInterval(interval);
          return 100;
        }
        return Math.min(prev + (100 - totalSteps), 100);
      });
    }, intervalDuration);
    return () => clearInterval(interval);
  }, []);

  console.log('percent: ', percent);
  return (
    <div
      className={
        !notification
          ? 'absolute top-20 right-0 z-40 w-1/4 px-3 py-4 shadow-md shadow-slate-600 rounded-md bg-slate-50 justify-center items-center gap-4 flex'
          : 'hidden'
      }
    >
      {icon}
      <div className='w-3/4 flex flex-col justify-start items-start'>
        <h3 className='text-slate-900 text-[16px] font-bold'>{message}</h3>
        <p className='text-slate-800 text-xs'>{description}</p>
        <Flex className='w-full' vertical gap='small'>
          <Flex vertical gap='small'>
            <Progress percent={percent} type='line' />
          </Flex>
        </Flex>
      </div>
    </div>
  );
};

export default Notification;
