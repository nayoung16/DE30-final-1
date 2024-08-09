// src/components/StyleFilterSelect.js
import React, { useState } from 'react';

const questions = [
  { id: 1, question: 'Do you prefer mountain or beach camping?', options: ['Mountain', 'Beach'] },
  { id: 2, question: 'Do you like to camp in a tent or an RV?', options: ['Tent', 'RV'] },
  // 추가 질문들을 여기에 추가
];

function StyleFilterSelect({ onStyleSelect }) {
  const [answers, setAnswers] = useState({});

  const handleSelect = (questionId, option) => {
    setAnswers({ ...answers, [questionId]: option });
  };

  const handleSubmit = () => {
    // 선택된 답변을 통해 캠핑 스타일을 산출합니다.
    const finalStyle = calculateStyle(answers);
    onStyleSelect(finalStyle);
  };

  const calculateStyle = (answers) => {
    // 답변을 기반으로 스타일 계산 로직 구현
    // 간단한 예시: 산악 캠핑 스타일 산출
    if (answers[1] === 'Mountain' && answers[2] === 'Tent') {
      return 'Mountain Tent Camping';
    }
    // 추가적인 스타일 계산 로직 추가
    return 'General Camping';
  };

  return (
    <div>
      {questions.map((q) => (
        <div key={q.id}>
          <h3>{q.question}</h3>
          {q.options.map((option) => (
            <button
              key={option}
              onClick={() => handleSelect(q.id, option)}
              className={answers[q.id] === option ? 'selected' : ''}
            >
              {option}
            </button>
          ))}
        </div>
      ))}
      <button onClick={handleSubmit}>See Results</button>
    </div>
  );
}

export default StyleFilterSelect;
