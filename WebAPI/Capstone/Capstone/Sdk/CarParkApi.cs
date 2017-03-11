﻿using AutoMapper.QueryableExtensions;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Sdk
{
    public partial class CarParkApi
    {
        public int GetEmptyAmount(int carParkId)
        {
            return this.BaseService.GetEmptyAmount(carParkId);
        }

        public IEnumerable<CarParkWithAmount> GetCoordinatesWithEmptyAmount()
        {
            var result = this.BaseService.GetCoordinatesWithEmptyAmount().ProjectTo<CarParkWithAmount>(this.AutoMapperConfig).ToList();
            return result;
        }

        public IEnumerable<CarParkViewModel> GetCarParksByUserId(string userId)
        {
            var result = this.BaseService.GetCarParksByUserId(userId).ProjectTo<CarParkViewModel>(this.AutoMapperConfig).ToList();
            return result;
        }
    }
}