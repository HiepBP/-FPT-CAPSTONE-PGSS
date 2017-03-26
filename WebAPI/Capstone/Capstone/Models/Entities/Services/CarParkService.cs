using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface ICarParkService
    {
        int GetEmptyAmount(int carParkId);
        IQueryable<CarParkWithAmountEntities> GetCoordinatesWithEmptyAmount();
        IQueryable<CarPark> GetCarParksByUserId(string userId);

    }

    public partial class CarParkService
    {
        /// <summary>
        /// Lấy số lượng chỗ còn trống trong bãi giữ xe
        /// </summary>
        /// <param name="carParkId"></param>
        /// <returns>
        /// int (-1 khi carPark đã bị Deacti-e hoặc không tìm thấy)
        /// </returns>
        public int GetEmptyAmount(int carParkId)
        {
            var carPark = this.Get(carParkId);
            if(carPark!= null && carPark.Active != false)
            {
                return carPark.ParkingLots.Count(q => q.Status == (int)ParkingLotStatus.Active);
            }
            return -1;
        }

        public IQueryable<CarParkWithAmountEntities> GetCoordinatesWithEmptyAmount()
        {
            var carPark = this.GetActive().Select(q => new CarParkWithAmountEntities()
            {
                CarPark = q,
                EmptyAmount = q.ParkingLots.Count(x => x.Status == (int)ParkingLotStatus.Active),
            });
            return carPark;
        }

        public IQueryable<CarPark> GetCarParksByUserId(string userId)
        {
            var carParks = this.GetActive(q => q.AspNetUserId == userId);
            return carParks;
        }

    }

    public class CarParkWithAmountEntities
    {
        public CarPark CarPark { get; set; }
        public int? EmptyAmount { get; set; }
    }
}