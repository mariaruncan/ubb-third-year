import { useContext, useEffect, useState } from "react";
import { Box, styled, Tab, Tabs } from "@mui/material";
import { MainPageContext } from "../../pages/MainPage";
import { HOST, PORT } from "../../prodURL";
import axios from "axios";
import { Viewer, Worker } from "@react-pdf-viewer/core";
import { defaultLayoutPlugin } from "@react-pdf-viewer/default-layout";
import '@react-pdf-viewer/default-layout/lib/styles/index.css';
import Quiz from "../Quiz/Quiz";

// interface TabPanelProps {
//     children?: React.ReactNode;
//     index: number;
//     value: number;
// }

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`simple-tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    <div>{children}</div>
                </Box>
            )}
        </div>
    );
}

export const TabsMenu = () => {
    const [value, setValue] = useState(0);
    const [chapter, setChapter] = useState(null);
    const [pdfUrl, setPdfUrl] = useState(null);
    const [pdf, setPdf] = useState(null);
    const context = useContext(MainPageContext);
    const selectedId = context.selectedId;
    const [quizQuestions, setQuizQuestions] = useState([]);
    const token = localStorage.getItem("token");

    useEffect(() => {
        let localChapterData;
        // get chapter details
        let url = `http://${HOST}:${PORT}/chapter/${selectedId}`;
        axios
            .get(url, {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })
            .then((resp) => {
                if (resp.status == 200) {
                    localChapterData = resp.data;
                    localChapterData.videoUrl = youtube_parser(localChapterData.videoUrl);
                    console.log(localChapterData);
                    setChapter(localChapterData);
                    setPdfUrl(`http://${HOST}:${PORT}/${localChapterData.pdfUrl}`);
                }
            })
            .catch((err) => {
                console.log(err);
            });

        let urlQuiz = `http://${HOST}:${PORT}/quiz`;
        axios
            .get(urlQuiz, {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })
            .then((resp) => {
                if (resp.status === 200) {
                    const questions = resp.data.find((quiz) => quiz.id === selectedId);
                    setQuizQuestions(questions);
                }
            })
            .catch((err) => {
                console.log(err);
            });

    }, [selectedId]);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    function a11yProps(index) {
        return {
            id: `simple-tab-${index}`,
            'aria-controls': `simple-tabpanel-${index}`,
        };
    };

    function youtube_parser(url) {
        var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
        var match = url.match(regExp);
        if (match && match[2].length === 11) {
            return match[2];
        } else {
            //error
        }
    };

    const DocumentWrapper = styled("div")({
        maxHeight: "600px",
        overflowY: "auto"
    });

    const youtubeID = 'KxqlJblhzfI';
    const defaultLayoutPluginInstance = defaultLayoutPlugin();

    return (
        <div style={{ marginLeft: '300px' }}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
                    <Tab label="Continut" {...a11yProps(0)} />
                    <Tab label="Material Video" {...a11yProps(1)} />
                    <Tab label="Quiz" {...a11yProps(2)} />
                </Tabs>
            </Box>
            <TabPanel value={value} index={0}
                style={{ width: '500px', height: '650px' }}
            >
                {pdfUrl !== null &&
                    <Worker workerUrl="https://unpkg.com/pdfjs-dist@3.4.120/build/pdf.worker.min.js">
                        <div
                            style={{
                                border: '1px solid rgba(0, 0, 0, 0.3)',
                                height: '750px',
                                width: '1100px',
                                overflow: 'auto'
                            }}
                        >
                            <Viewer fileUrl={pdfUrl}
                                plugins={[defaultLayoutPluginInstance]}
                                httpHeaders={
                                    {
                                        Authorization: `Bearer ${token}`,
                                    }
                                }
                            />
                        </div>
                    </Worker>
                }
            </TabPanel>
            <TabPanel value={value} index={1}>
                <iframe style={{ width: '1100px', height: '550px' }}
                    className='video'
                    title='Youtube player'
                    sandbox='allow-same-origin allow-forms allow-popups allow-scripts allow-presentation'
                    src={`https://youtube.com/embed/${chapter?.videoUrl}`}>
                </iframe>
            </TabPanel>
            <TabPanel value={value} index={2}>
                <Quiz quizQuestions={quizQuestions} quizId={selectedId} />
            </TabPanel>
        </div>
    )
}