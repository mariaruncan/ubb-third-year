import { React, useState } from "react";

const Quiz = ({ quizQuestions, quizId }) => {

    const [answers, setAnswers] = useState([]);

    const handleAnswerChange = (questionIndex, answerIndex) => {
        const newAnswers = [...answers];
        newAnswers[questionIndex] = answerIndex;
        setAnswers(newAnswers);
    };

    const handleSubmit = () => {
        console.log('Submitted answers:', answers);
        console.log(quizId);

        //submit quiz
    };

    return (
        <div>
            {quizQuestions.questions.map((question, index) => (
                <div key={question.id} style={{ marginBottom: 25 }}>
                    <h3>{index + 1 + ') ' + question.text}</h3>
                    {question.options.map((option, optionIndex) => (
                        <div key={optionIndex} style={{ marginBottom: 20, marginLeft: 20 }}>
                            <input
                                type="radio"
                                name={`question${index}`}
                                value={optionIndex}
                                checked={answers[index] === optionIndex}
                                onChange={() => handleAnswerChange(index, optionIndex)}
                            />
                            {option}
                        </div>
                    ))}
                </div>
            ))}

            <button onClick={handleSubmit} style={{
                marginTop: 15,
                width: 80,
                height: 30,
                fontSize: 20,
                textAlign: 'center',
                backgroundColor: '#f59d37e6',
                cursor: 'pointer',
                borderRadius: 10,
                borderColor: '#f59d37e6'
            }}>Submit</button>
        </div >
    );
};

export default Quiz;
