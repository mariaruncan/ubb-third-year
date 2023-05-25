import React, { createContext, useContext, useState } from 'react'
import { Navbar } from '../components/Navbar/Navbar';
import { Menu } from '../components/Menu/Menu';
import { TabsMenu } from "../components/TabsMenu/TabsMenu";

export const MainPageContext = createContext();

const MainPage = () => {

    const [selectedId, setSelectedId] = useState(1);
    
    return (
        <MainPageContext.Provider value={{
            selectedId, setSelectedId
        }}>
            <div>
                <Navbar />
                <Menu />
                <TabsMenu />
            </div>
        </MainPageContext.Provider>

    );
};

export default MainPage;