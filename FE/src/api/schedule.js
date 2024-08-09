// api/schedule.js
const API_URL = process.env.REACT_APP_API_URL;

export const createSchedule = async (scheduleData) => {
  try {
    const response = await fetch(`${API_URL}/sch/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(scheduleData),
    });

    if (!response.ok) throw new Error('Failed to create schedule');
    return response.json();
  } catch (e) {
    console.error('createSchedule Error: ', e.message);
    return false;
  }
};

export const getAllSchedules = async () => {
  try {
    const response = await fetch(`${API_URL}/sch/selectAll`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) throw new Error('Failed to fetch schedules');
    return response.json();
  } catch (e) {
    console.error('getAllSchedules Error: ', e.message);
    return false;
  }
};


export const updateSchedule = async (schId, scheduleData) => {
  try {
    const response = await fetch(`${API_URL}/sch/update`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include', // 쿠키 인증이 필요한 경우 추가
      body: JSON.stringify({
        schId, // ID를 직접 전달
        ...scheduleData, // 나머지 데이터
      }),
    });

    if (!response.ok) {
      throw new Error(`Failed to update schedule: ${response.statusText}`);
    }

    const updatedSchedule = await response.json(); // 서버에서 응답 받은 JSON을 파싱
    return updatedSchedule;
  } catch (error) {
    console.error('updateSchedule Error:', error.message);
    return null; // 실패 시 null 반환
  }
};

export const deleteSchedule = async (schId) => {
  try {
    const response = await fetch(`${API_URL}/sch/delete/${schId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include', // 필요에 따라 추가, 쿠키 인증이 필요한 경우
    });

    if (!response.ok) throw new Error('Failed to delete schedule');
    return true;
  } catch (error) {
    console.error('deleteSchedule Error:', error.message);
    return null;
  }
};