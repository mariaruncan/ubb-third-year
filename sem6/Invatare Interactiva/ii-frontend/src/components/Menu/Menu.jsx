import React, {useContext, useState} from 'react';
import './Menu.css';
import axios from "axios";
import { HOST, PORT } from '../../prodURL';
import {MainPageContext} from "../../pages/MainPage";

export const Menu = () => {
    const context = useContext(MainPageContext);
    const [chapters, setChapters] = useState([]);
    const selectedId = context.selectedId;
    const setSelectedId = context.setSelectedId;

    const token = localStorage.getItem("token");
    React.useEffect(() => {
        const url = `http://${HOST}:${PORT}/chapter`;
        axios
            .get(url, {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })
            .then((resp) => {
                if (resp.status == 200) {
                    setChapters(resp.data);
                    setSelectedId(resp.data[0].id);
                }
            })
            .catch((err) => {
                console.log(err);
            });

    }, []);

    const handleItemClick = (id) => {
        setSelectedId(id);
    };

    return (
        <div className="left-menu">
            {chapters.map((chapter) => (
                <div
                    key={chapter.id}
                    className={selectedId === chapter.id ? 'selected' : ''}
                    onClick={() => handleItemClick(chapter.id)}
                >
                    {chapter.title}
                </div>
            ))}
        </div>
    );
};