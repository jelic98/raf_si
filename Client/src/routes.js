import Login from "./components/Login";
import Register from "./components/Register";

import ManageProjects from "./components/ManageProjects";
import Project from "./components/Project";

import ManageTeams from "./components/ManageTeams";
import RequirementsModel from "./components/RequirementsModel";
import ClassModel from "./components/ClassModel";
import Profile from "./components/Profile";
import UseCase from "./components/UseCase";

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
        component: ManageProjects,
        // meta: {
        //     requires_login: true
        // }
    },
    {
        path: '/projects/:project_name',
        component: Project,
        props: true,
        // meta: {
        //     requires_login: true
        // }
    },
    {
        path: '/projects/:project_name/models/:model_name/requirements',
        component: RequirementsModel,
        props: true,
        // meta: {
        //     requires_login: true
        // }
    },
	{
        path: '/projects/:project_name/models/:model_name/class',
        component: ClassModel,
        props: true,
        // meta: {
        //     requires_login: true
        // }
    },
    {
        path: '/projects/:project_name/models/:model_name/functional',
        component: UseCase,
        // meta: {
        //     requires_login: true
        // }
    },
    {
        path: '/teams',
        component: ManageTeams,
        meta: {
            requires_login: true
        }
    },
    {
        path: '/profile',
        component: Profile,
        meta: {
            requires_login: true
        }
    }
];

export default routes;
