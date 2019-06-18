import axios from 'axios'
import {ACCESS_TOKEN, ONLINE_API_URL} from '../../constants/constants'
import Cookie from 'react-cookies'

const request = (config) => {
  let headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'X-XSRF-TOKEN': Cookie.load('XSRF-TOKEN')
  };
  if (localStorage.getItem(ACCESS_TOKEN)) {
    headers = {...headers, 'Authorization': 'Bearer ' + localStorage.getItem(ACCESS_TOKEN)}
  }

  const instance = axios.create({
    baseURL: ONLINE_API_URL,
    headers: headers,
    withCredentials: true
  });

  return instance.request(config);
};

export function loginUser(loginRequest) {

  return request({
    url: '/auth/signin',
    method: 'post',
    data: loginRequest
  });
}

export function isEmailAvailable(email) {
  return request({
    url: `/users/checkEmailAvailability/${email}`,
    data: email
  });
}

export function signUpUser(signUpRequest) {
  return request({
    url: '/auth/signup',
    method: 'post',
    data: signUpRequest
  })
}

export function editUser(editRequest) {
  return request({
    url: '/users',
    method: 'put',
    data: editRequest
  })
}

export function getCurrentUser() {
  if (!localStorage.getItem(ACCESS_TOKEN)) {
    return Promise.reject("No access token set.");
  }
  return request({
    url: '/users/me'
  });
}

export function getUserProfile(id) {
  return request({
    url: `/users/${id}`
  });
}

export function getAllEvents() {
  return request({
    url: `/events`
  });
}

export function getEvent(id) {
  return request({
    url: '/events/' + id,
    method: 'get'
  });
}

export function getCategory(id) {
  return request({
    url: '/categories/' + id,
    method: 'get'
  });
}

export function editEvent(event) {
  return request({
    url: '/events',
    method: 'put',
    data: event
  });
}

export function getAllCategories() {
  return request({
    url: '/categories',
    method: 'get'
  });
}

export function renderAvatar(id) {
  return request({
    url: `/users/${id}/image`,
  });
}

export function createCategory(newCategory) {
  return request({
    url: '/categories',
    method: 'post',
    data: newCategory,
  });
}

export function editCategory(categoryRequest) {
  return request({
    url: `/categories`,
    method: 'put',
    data: categoryRequest,
  });
}

export function createEvent(newEvent) {
  return request({
    url: '/events',
    method: 'post',
    data: newEvent
  });
}

export function getEventPage(page, size, eventFilter) {
  return request({
    url: '/events/page',
    params: {...eventFilter, page: page, size: size}
  })
}

export function getParticipantByUserId(userId) {
  return request({
    url: `/participants`,
    params: {userId: userId}
  })
}

export function changePassword (password) {
  return request({
    url: '/users/password',
    method: 'put',
    data: password
  });
}

export function getParticipantByEventId(eventId) {
  return request({
    url: '/events/' + eventId + '/participants',
  })
}

export function participateInEvent(participant) {
  return request({
    url: '/participants',
    method: 'post',
    data: participant
  })

}