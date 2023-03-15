import Router from 'koa-router';
import scooterStore from './store';
import {broadcast} from "../utils";

export const router = new Router();

router.get('/', async (ctx) => {
    const response = ctx.response;
    const userId = ctx.state.user._id;
    response.body = await scooterStore.find({userId});
    response.status = 200; // ok
});

router.get('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const scooter = await scooterStore.findOne({_id: ctx.params.id});
    const response = ctx.response;
    if (scooter) {
        if (scooter.userId === userId) {
            response.body = scooter;
            response.status = 200; // ok
        } else {
            response.status = 403; // forbidden
        }
    } else {
        response.status = 404; // not found
    }
});

const createScooter = async (ctx, scooter, response) => {
    try {
        const userId = ctx.state.user._id;
        scooter.userId = userId;
        response.body = await scooterStore.insert(scooter);
        response.status = 201; // created
        broadcast(userId, {type: 'created', payload: scooter});
    } catch (err) {
        response.body = {message: err.message};
        response.status = 400; // bad request
    }
};

router.post('/', async ctx => await createScooter(ctx, ctx.request.body, ctx.response));

router.put('/:id', async (ctx) => {
    const scooter = ctx.request.body;
    const id = ctx.params.id;
    const scooterId = scooter._id;
    const response = ctx.response;
    if (scooterId && scooterId !== id) {
        response.body = {message: 'Param id and body _id should be the same'};
        response.status = 400; // bad request
        return;
    }
    if (!scooterId) {
        await createScooter(ctx, scooter, response);
    } else {
        const userId = ctx.state.user._id;
        scooter.userId = userId;
        const updatedCount = await scooterStore.update({_id: id}, scooter);
        if (updatedCount === 1) {
            response.body = scooter;
            response.status = 200; // ok
            broadcast(userId, {type: 'updated', payload: scooter});
        } else {
            response.body = {message: 'Resource no longer exists'};
            response.status = 405; // method not allowed
        }
    }
});

router.del('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const scooter = await scooterStore.findOne({_id: ctx.params.id});
    if (scooter && userId !== scooter.userId) {
        ctx.response.status = 403; // forbidden
    } else {
        await scooterStore.remove({_id: ctx.params.id});
        ctx.response.status = 204; // no content
    }
});
