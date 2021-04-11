import Login from "./components/Login";
import Register from "./components/Register";

import ManageProjects from "./components/ManageProjects";
import Project from "./components/Project";

import ManageTeams from "./components/ManageTeams";
import RequirementsModel from "./components/RequirementsModel";
import Profile from "./components/Profile";

const routes = [
    {
        path: '/login',
        alias: '/',
        component: Login
    },
    {
        path: '/register',
        component: Register
    },
    {
        path: '/projects',
        component: ManageProjects
    },
    {
        path: '/projects/:project_name',
        component: Project,
        props: true
    },
    {
        path: '/projects/:project_name/models/:model_name',
        component: RequirementsModel,
        props: true
    },
    {
        path: '/teams',
        component: ManageTeams
    },
    {
        path: '/profile',
        component: Profile
    }
];

export default routes;